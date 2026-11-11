package Finance.organizador_financeiro.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import Finance.organizador_financeiro.domain.Category;
import Finance.organizador_financeiro.domain.Transaction;
import Finance.organizador_financeiro.domain.TipoLancamento;
import Finance.organizador_financeiro.domain.User;
import Finance.organizador_financeiro.dto.TransactionDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {

    private final TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);

    @Test
    void toDtoMapsTransactionTypeAndRelatedIdsToApiContract() {
        Category category = new Category();
        category.setId(8L);
        User user = new User();
        user.setId(3L);
        Transaction transaction = new Transaction();
        transaction.setId(12L);
        transaction.setAmount(new BigDecimal("25.50"));
        transaction.setDate(LocalDate.of(2026, 11, 10));
        transaction.setDescription("Lunch");
        transaction.setCategory(category);
        transaction.setUser(user);
        transaction.setType(TipoLancamento.RECEITA);

        TransactionDTO dto = mapper.toDTO(transaction);

        assertEquals(12L, dto.getId());
        assertEquals(8L, dto.getCategoryId());
        assertEquals(3L, dto.getUserId());
        assertEquals("INCOME", dto.getType());
    }

    @Test
    void toEntityMapsApiTypeAndLeavesOwnershipAndCategoryForService() {
        TransactionDTO dto = new TransactionDTO(
                12L,
                new BigDecimal("25.50"),
                LocalDate.of(2026, 11, 10),
                "Lunch",
                3L,
                8L,
                "EXPENSE"
        );

        Transaction transaction = mapper.toEntity(dto);

        assertNull(transaction.getId());
        assertNull(transaction.getUser());
        assertNull(transaction.getCategory());
        assertEquals(TipoLancamento.DESPESA, transaction.getType());
        assertEquals(dto.getAmount(), transaction.getAmount());
        assertEquals(dto.getDate(), transaction.getDate());
        assertEquals(dto.getDescription(), transaction.getDescription());
    }

    @Test
    void toEntityRejectsUnsupportedTransactionType() {
        TransactionDTO dto = new TransactionDTO();
        dto.setType("TRANSFER");

        assertThrows(IllegalArgumentException.class, () -> mapper.toEntity(dto));
    }

    @Test
    void toEntityAcceptsLowercaseMobileTransactionTypes() {
        TransactionDTO dto = new TransactionDTO();
        dto.setType("expense");

        assertEquals(TipoLancamento.DESPESA, mapper.toEntity(dto).getType());
    }

    @Test
    void transactionDtoAcceptsMobileDateFieldAlias() throws Exception {
        TransactionDTO dto = new ObjectMapper().findAndRegisterModules().readValue(
                """
                {
                  "transactionDate": "2026-11-10",
                  "type": "expense"
                }
                """,
                TransactionDTO.class
        );

        assertEquals(LocalDate.of(2026, 11, 10), dto.getDate());
        assertEquals("expense", dto.getType());
    }
}
