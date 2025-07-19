package Finance.organizador_financeiro.mapper;

import Finance.organizador_financeiro.domain.TransactionType;
import org.mapstruct.Mapper;
import Finance.organizador_financeiro.dto.TransactionTypeDTO;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionTypeMapper {
    TransactionTypeDTO toDTO(TransactionType entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TransactionType toEntity(TransactionTypeDTO dto);
}