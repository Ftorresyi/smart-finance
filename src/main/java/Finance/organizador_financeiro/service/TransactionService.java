package Finance.organizador_financeiro.service;

import Finance.organizador_financeiro.domain.Transaction;
import Finance.organizador_financeiro.mapper.TransactionMapper;
import Finance.organizador_financeiro.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    public TransactionService(TransactionRepository repository, TransactionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    public List<Transaction> findAll() {
        return repository.findAll();
    }
}
