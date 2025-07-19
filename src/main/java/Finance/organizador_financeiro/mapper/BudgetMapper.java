package Finance.organizador_financeiro.mapper;

import Finance.organizador_financeiro.domain.Budget;
import Finance.organizador_financeiro.domain.Category;
import Finance.organizador_financeiro.dto.BudgetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.YearMonth;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class}) // Added CategoryMapper for Category mapping
public interface BudgetMapper {

    BudgetMapper INSTANCE = Mappers.getMapper(BudgetMapper.class);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(target = "period", expression = "java(budget.getPeriod().toString())")
    BudgetDTO toDTO(Budget budget);

    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategoryFromId")
    @Mapping(target = "user", ignore = true) // The user will be set in the service
    @Mapping(target = "period", expression = "java(java.time.YearMonth.parse(budgetDTO.getPeriod()))") // Use fully qualified name
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