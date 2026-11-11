package Finance.organizador_financeiro.mapper;

import Finance.organizador_financeiro.domain.Transaction;
import Finance.organizador_financeiro.domain.TipoLancamento;
import Finance.organizador_financeiro.dto.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Locale;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "type", target = "type", qualifiedByName = "toApiType")
    TransactionDTO toDTO(Transaction entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(source = "type", target = "type", qualifiedByName = "toDomainType")
    Transaction toEntity(TransactionDTO dto);

    @Named("toApiType")
    default String toApiType(TipoLancamento type) {
        if (type == null) {
            return null;
        }
        return switch (type) {
            case RECEITA -> "INCOME";
            case DESPESA -> "EXPENSE";
        };
    }

    @Named("toDomainType")
    default TipoLancamento toDomainType(String type) {
        if (type == null) {
            return null;
        }
        return switch (type.toUpperCase(Locale.ROOT)) {
            case "INCOME" -> TipoLancamento.RECEITA;
            case "EXPENSE" -> TipoLancamento.DESPESA;
            default -> throw new IllegalArgumentException("Unsupported transaction type: " + type);
        };
    }
}
