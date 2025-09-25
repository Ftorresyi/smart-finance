package Finance.organizador_financeiro.mapper;


import Finance.organizador_financeiro.domain.Category;
import Finance.organizador_financeiro.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDTO(Category entity);

    Category toEntity(CategoryDTO dto);
}
