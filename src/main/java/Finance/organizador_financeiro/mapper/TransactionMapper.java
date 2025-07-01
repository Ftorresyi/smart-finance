package Finance.organizador_financeiro.mapper;


import Finance.organizador_financeiro.domain.Transaction;
import Finance.organizador_financeiro.dto.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "type", target = "type")
    public TransactionDTO toDTO(Transaction entity);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "userId", target = "user.id")
    @Mapping (source = "type", target = "type")
    public Transaction toEntity(TransactionDTO dto);
}
