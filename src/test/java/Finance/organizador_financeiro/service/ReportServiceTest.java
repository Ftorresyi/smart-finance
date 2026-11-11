package Finance.organizador_financeiro.service;

import Finance.organizador_financeiro.domain.Category;
import Finance.organizador_financeiro.domain.Transaction;
import Finance.organizador_financeiro.domain.TipoLancamento;
import Finance.organizador_financeiro.domain.User;
import Finance.organizador_financeiro.repository.TransactionRepository;
import Finance.organizador_financeiro.security.AuthenticationComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuthenticationComponent authenticationComponent;

    @InjectMocks
    private ReportService service;

    private User authenticatedUser;

    @BeforeEach
    void setUp() {
        authenticatedUser = new User();
        authenticatedUser.setId(7L);
        when(authenticationComponent.getAuthenticatedUser()).thenReturn(authenticatedUser);
    }

    @Test
    void summarySeparatesIncomeAndExpensesForAuthenticatedUser() {
        LocalDate start = LocalDate.of(2026, 11, 1);
        LocalDate end = LocalDate.of(2026, 11, 30);
        when(transactionRepository.findByUserAndDateBetween(authenticatedUser, start, end))
                .thenReturn(List.of(
                        transaction("100.00", TipoLancamento.RECEITA, null),
                        transaction("35.50", TipoLancamento.DESPESA, null)
                ));

        var summary = service.getSummaryReport(start, end);

        assertEquals(new BigDecimal("100.00"), summary.getTotalIncome());
        assertEquals(new BigDecimal("35.50"), summary.getTotalExpenses());
        assertEquals(new BigDecimal("64.50"), summary.getNetBalance());
        verify(transactionRepository).findByUserAndDateBetween(authenticatedUser, start, end);
    }

    @Test
    void spendingByCategoryIncludesOnlyExpenses() {
        LocalDate start = LocalDate.of(2026, 11, 1);
        LocalDate end = LocalDate.of(2026, 11, 30);
        Category food = new Category();
        food.setName("Food");
        when(transactionRepository.findByUserAndDateBetween(authenticatedUser, start, end))
                .thenReturn(List.of(
                        transaction("20.00", TipoLancamento.DESPESA, food),
                        transaction("15.00", TipoLancamento.DESPESA, food),
                        transaction("100.00", TipoLancamento.RECEITA, food)
                ));

        var spending = service.getSpendingByCategory(start, end);

        assertEquals(1, spending.size());
        assertEquals("Food", spending.get(0).getCategoryName());
        assertEquals(new BigDecimal("35.00"), spending.get(0).getTotalSpent());
    }

    @Test
    void monthlyReportsAggregateIncomeAndExpensesByMonth() {
        when(transactionRepository.findByUserAndDateBetween(
                eq(authenticatedUser),
                eq(LocalDate.of(2026, 1, 1)),
                eq(LocalDate.of(2026, 12, 31))))
                .thenReturn(List.of(
                        transaction("100.00", TipoLancamento.RECEITA, null),
                        transaction("30.00", TipoLancamento.DESPESA, null)
                ));

        var reports = service.getMonthlyReports(2026);

        assertEquals(1, reports.size());
        assertEquals("2026-11", reports.get(0).getPeriod());
        assertEquals(new BigDecimal("100.00"), reports.get(0).getTotalIncome());
        assertEquals(new BigDecimal("30.00"), reports.get(0).getTotalExpenses());
        assertEquals(new BigDecimal("70.00"), reports.get(0).getNetBalance());
    }

    @Test
    void annualReportsAggregateIncomeAndExpensesByYear() {
        when(transactionRepository.findByUserAndDateBetween(
                eq(authenticatedUser),
                eq(LocalDate.of(2026, 1, 1)),
                eq(LocalDate.of(2027, 12, 31))))
                .thenReturn(List.of(
                        transaction("100.00", TipoLancamento.RECEITA, null),
                        transaction("30.00", TipoLancamento.DESPESA, null)
                ));

        var reports = service.getAnnualReports(2026, 2027);

        assertEquals(1, reports.size());
        assertEquals("2026", reports.get(0).getPeriod());
        assertEquals(new BigDecimal("100.00"), reports.get(0).getTotalIncome());
        assertEquals(new BigDecimal("30.00"), reports.get(0).getTotalExpenses());
        assertEquals(new BigDecimal("70.00"), reports.get(0).getNetBalance());
    }

    private Transaction transaction(String amount, TipoLancamento type, Category category) {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(amount));
        transaction.setType(type);
        transaction.setCategory(category);
        transaction.setDate(LocalDate.of(2026, 11, 5));
        return transaction;
    }
}
