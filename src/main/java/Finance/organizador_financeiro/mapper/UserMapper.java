package Finance.organizador_financeiro.mapper;


import Finance.organizador_financeiro.domain.User;
import Finance.organizador_financeiro.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User entity);

    User toEntity(UserDTO dto);

}
