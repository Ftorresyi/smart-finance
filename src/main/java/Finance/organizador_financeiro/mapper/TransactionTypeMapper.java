package Finance.organizador_financeiro.mapper;


import Finance.organizador_financeiro.domain.TransactionType;
import org.mapstruct.Mapper;
import Finance.organizador_financeiro.dto.TransactionTypeDTO;

@Mapper(componentModel = "spring")

public interface TransactionTypeMapper {
    TransactionTypeDTO toDTO(TransactionType entity);

    TransactionType toEntity(TransactionTypeDTO dto);
}
