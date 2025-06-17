package Finance.organizador_financeiro.mapper;


import Finance.organizador_financeiro.domain.Category;
import Finance.organizador_financeiro.dto.CategoryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    public CategoryDTO toDTO(Category entity);

    public Category toEntity(CategoryDTO dto);
}
