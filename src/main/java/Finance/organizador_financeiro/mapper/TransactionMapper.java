package Finance.organizador_financeiro.mapper;


import Finance.organizador_financeiro.domain.Transaction;
import Finance.organizador_financeiro.dto.TransactionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    public TransactionDTO toDTO(Transaction entity);

    public Transaction toEntity(TransactionDTO dto);
}
