package Finance.organizador_financeiro.mapper;

import Finance.organizador_financeiro.domain.Budget;
import Finance.organizador_financeiro.domain.Category;
import Finance.organizador_financeiro.dto.BudgetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
@Mapper(componentModel = "spring")
public interface BudgetMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "period", expression = "java(budget.getPeriod().toString())")
    BudgetDTO toDTO(Budget budget);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "period", expression = "java(java.time.YearMonth.parse(budgetDTO.getPeriod()))")
    Budget toEntity(BudgetDTO budgetDTO);

    // Helper method to map Category from ID, if necessary
    @Named("mapCategoryFromId")
    default Category mapCategoryFromId(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }
}