package Finance.organizador_financeiro.service;

import Finance.organizador_financeiro.domain.Transaction;
import Finance.organizador_financeiro.domain.TipoLancamento;
import Finance.organizador_financeiro.domain.User;
import Finance.organizador_financeiro.dto.CategorySpendingDTO;
import Finance.organizador_financeiro.dto.SummaryReportDTO;
import Finance.organizador_financeiro.repository.TransactionRepository;
import Finance.organizador_financeiro.security.AuthenticationComponent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final AuthenticationComponent authenticationComponent;

    public ReportService(TransactionRepository transactionRepository, AuthenticationComponent authenticationComponent) {
        this.transactionRepository = transactionRepository;
        this.authenticationComponent = authenticationComponent;
    }

    @Transactional(readOnly = true)
    public SummaryReportDTO getSummaryReport(LocalDate startDate, LocalDate endDate) {
        User user = authenticationComponent.getAuthenticatedUser();
        List<Transaction> transactions = transactionRepository.findByUserAndDateBetween(user, startDate, endDate);

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() != null && t.getType().getName().equals(TipoLancamento.RECEITA.name()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = transactions.stream()
                .filter(t -> t.getType() != null && t.getType().getName().equals(TipoLancamento.DESPESA.name()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        return new SummaryReportDTO(totalIncome, totalExpenses, netBalance);
    }

    @Transactional(readOnly = true)
    public List<CategorySpendingDTO> getSpendingByCategory(LocalDate startDate, LocalDate endDate) {
        User user = authenticationComponent.getAuthenticatedUser();
        List<Transaction> transactions = transactionRepository.findByUserAndDateBetween(user, startDate, endDate);

        Map<String, BigDecimal> spendingMap = transactions.stream()
                .filter(t -> t.getType() != null && t.getType().getName().equals(TipoLancamento.DESPESA.name()))
                .collect(Collectors.groupingBy(
                        t -> t.getCategory() != null ? t.getCategory().getName() : "Uncategorized",
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        return spendingMap.entrySet().stream()
                .map(entry -> new CategorySpendingDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
