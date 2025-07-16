package Finance.organizador_financeiro.service;

import Finance.organizador_financeiro.domain.Transaction;
import Finance.organizador_financeiro.domain.User;
import Finance.organizador_financeiro.dto.TransactionDTO;
import Finance.organizador_financeiro.mapper.TransactionMapper;
import Finance.organizador_financeiro.repository.TransactionRepository;
import Finance.organizador_financeiro.security.AuthenticationComponent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final AuthenticationComponent authenticationComponent;

    public TransactionService(TransactionRepository repository, TransactionMapper mapper, AuthenticationComponent authenticationComponent) {
        this.repository = repository;
        this.mapper = mapper;
        this.authenticationComponent = authenticationComponent;
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> findAll() {
        User user = authenticationComponent.getAuthenticatedUser();
        return repository.findByUser(user).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<TransactionDTO> findById(Long id) {
        User user = authenticationComponent.getAuthenticatedUser();
        return repository.findByIdAndUser(id, user)
                .map(mapper::toDTO);
    }

    @Transactional
    public TransactionDTO create(TransactionDTO dto) {
        User user = authenticationComponent.getAuthenticatedUser();
        Transaction entity = mapper.toEntity(dto);
        entity.setUser(user); // Associa a transação ao usuário logado
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public Optional<TransactionDTO> update(Long id, TransactionDTO dto) {
        User user = authenticationComponent.getAuthenticatedUser();
        return repository.findByIdAndUser(id, user).map(existingTransaction -> {
            Transaction updatedTransaction = mapper.toEntity(dto);
            updatedTransaction.setId(id); // Garante que o ID seja o mesmo
            updatedTransaction.setUser(user); // Garante que o dono da transação não seja alterado
            return mapper.toDTO(repository.save(updatedTransaction));
        });
    }

    @Transactional
    public boolean delete(Long id) {
        User user = authenticationComponent.getAuthenticatedUser();
        return repository.findByIdAndUser(id, user).map(transaction -> {
            repository.delete(transaction);
            return true;
        }).orElse(false);
    }
}
