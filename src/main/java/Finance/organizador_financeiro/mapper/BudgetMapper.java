package Finance.organizador_financeiro.mapper;

import Finance.organizador_financeiro.domain.Budget;
import Finance.organizador_financeiro.domain.Category;
import Finance.organizador_financeiro.dto.BudgetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.YearMonth;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class}) // 'uses' para que o MapStruct saiba como mapear Category
public interface BudgetMapper {

    BudgetMapper INSTANCE = Mappers.getMapper(BudgetMapper.class);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(target = "period", expression = "java(budget.getPeriod().toString())")
    BudgetDTO toDTO(Budget budget);

    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategoryFromId")
    @Mapping(target = "user", ignore = true) // O usuário será definido no serviço
    @Mapping(target = "period", expression = "java(YearMonth.parse(budgetDTO.getPeriod()))")
    Budget toEntity(BudgetDTO budgetDTO);

    // Método auxiliar para mapear Category a partir do ID, se necessário
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
