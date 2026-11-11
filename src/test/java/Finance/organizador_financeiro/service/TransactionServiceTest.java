package Finance.organizador_financeiro.service;

import Finance.organizador_financeiro.domain.Category;
import Finance.organizador_financeiro.domain.Transaction;
import Finance.organizador_financeiro.domain.User;
import Finance.organizador_financeiro.dto.TransactionDTO;
import Finance.organizador_financeiro.mapper.TransactionMapper;
import Finance.organizador_financeiro.repository.CategoryRepository;
import Finance.organizador_financeiro.repository.TransactionRepository;
import Finance.organizador_financeiro.security.AuthenticationComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionMapper mapper;

    @Mock
    private AuthenticationComponent authenticationComponent;

    @InjectMocks
    private TransactionService service;

    private User authenticatedUser;

    @BeforeEach
    void setUp() {
        authenticatedUser = new User();
        authenticatedUser.setId(7L);
        when(authenticationComponent.getAuthenticatedUser()).thenReturn(authenticatedUser);
    }

    @Test
    void createAssociatesTransactionWithAuthenticatedUser() {
        TransactionDTO input = new TransactionDTO();
        input.setCategoryId(4L);
        Transaction transaction = new Transaction();
        Category category = new Category();
        TransactionDTO savedDto = new TransactionDTO();
        when(mapper.toEntity(input)).thenReturn(transaction);
        when(categoryRepository.findByIdAndUser(4L, authenticatedUser)).thenReturn(Optional.of(category));
        when(repository.save(transaction)).thenReturn(transaction);
        when(mapper.toDTO(transaction)).thenReturn(savedDto);

        assertSame(savedDto, service.create(input));
        assertSame(authenticatedUser, transaction.getUser());
        assertSame(category, transaction.getCategory());
        verify(repository).save(transaction);
    }

    @Test
    void updateUsesAuthenticatedUserAndPreservesOwnership() {
        long transactionId = 11L;
        TransactionDTO input = new TransactionDTO();
        input.setCategoryId(4L);
        Transaction existing = new Transaction();
        Transaction updated = new Transaction();
        Category category = new Category();
        when(repository.findByIdAndUser(transactionId, authenticatedUser)).thenReturn(Optional.of(existing));
        when(mapper.toEntity(input)).thenReturn(updated);
        when(categoryRepository.findByIdAndUser(4L, authenticatedUser)).thenReturn(Optional.of(category));
        when(repository.save(updated)).thenReturn(updated);
        when(mapper.toDTO(updated)).thenReturn(input);

        assertTrue(service.update(transactionId, input).isPresent());

        ArgumentCaptor<Transaction> savedTransaction = ArgumentCaptor.forClass(Transaction.class);
        verify(repository).save(savedTransaction.capture());
        assertEquals(transactionId, savedTransaction.getValue().getId());
        assertSame(authenticatedUser, savedTransaction.getValue().getUser());
        assertSame(category, savedTransaction.getValue().getCategory());
    }

    @Test
    void createRejectsCategoryNotOwnedByAuthenticatedUser() {
        TransactionDTO input = new TransactionDTO();
        input.setCategoryId(4L);
        when(mapper.toEntity(input)).thenReturn(new Transaction());
        when(categoryRepository.findByIdAndUser(4L, authenticatedUser)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.create(input));
        verify(repository, never()).save(any());
    }

    @Test
    void updateDoesNotSaveTransactionOwnedByAnotherUser() {
        long transactionId = 11L;
        when(repository.findByIdAndUser(transactionId, authenticatedUser)).thenReturn(Optional.empty());

        assertTrue(service.update(transactionId, new TransactionDTO()).isEmpty());
        verify(repository, never()).save(any());
    }

    @Test
    void deleteDoesNotDeleteTransactionOwnedByAnotherUser() {
        long transactionId = 11L;
        when(repository.findByIdAndUser(transactionId, authenticatedUser)).thenReturn(Optional.empty());

        assertFalse(service.delete(transactionId));
        verify(repository, never()).delete(any());
    }
}
