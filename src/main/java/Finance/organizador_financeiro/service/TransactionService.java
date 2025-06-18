package Finance.organizador_financeiro.service;

import Finance.organizador_financeiro.domain.Transaction;
import Finance.organizador_financeiro.dto.TransactionDTO;
import Finance.organizador_financeiro.mapper.TransactionMapper;
import Finance.organizador_financeiro.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    public TransactionService(TransactionRepository repository, TransactionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    public List<TransactionDTO> findAll() {
        return repository.findAll();
    }
    public TransactionDTO create(TransactionDTO dto) {
        Transaction entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }
    public Optional<TransactionDTO> update(Long id, TransactionDTO dto){
        return repository.findById(id).map(existing -> {
            Transaction updated = mapper.toEntity(dto);
            updated.setId(id); //mantém o id atual
            return mapper.toDTO(repository.save(updated));
        });
    }
    public void delete(Long id){
        repository.deleteById(id);
    }
}
