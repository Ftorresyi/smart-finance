package Finance.organizador_financeiro.mapper;


import Finance.organizador_financeiro.domain.User;
import Finance.organizador_financeiro.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    User toEntity(UserDTO dto);

}
