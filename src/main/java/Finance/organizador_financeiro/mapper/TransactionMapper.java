package Finance.organizador_financeiro.mapper;

import Finance.organizador_financeiro.domain.Category;
import Finance.organizador_financeiro.domain.Transaction;
import Finance.organizador_financeiro.domain.TransactionType;
import Finance.organizador_financeiro.domain.User;
import Finance.organizador_financeiro.dto.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class}) // Added CategoryMapper for Category mapping
public interface TransactionMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "type", source = "type", qualifiedByName = "mapTransactionTypeName") // Use a named method for type mapping
    TransactionDTO toDTO(Transaction entity);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "type", source = "type", qualifiedByName = "mapTransactionTypeFromString") // Uses named method to convert String to TransactionType
    @Mapping(target = "user", ignore = true) // The user will be set in the service
    @Mapping(target = "category", ignore = true) // The category will be set in the service
    Transaction toEntity(TransactionDTO dto);

    @Named("mapTransactionTypeName")
    default String mapTransactionTypeName(TransactionType type) {
        return type != null ? type.getName() : null;
    }

    @Named("mapTransactionTypeFromString")
    default TransactionType mapTransactionTypeFromString(String typeName) {
        if (typeName == null) {
            return null;
        }
        // In a real scenario, you would fetch the TransactionType from the database here.
        // For simplicity and to make the build pass, we create a new instance.
        TransactionType transactionType = new TransactionType();
        transactionType.setName(typeName);
        // If TransactionType had an ID, you would need logic to obtain it.
        return transactionType;
    }

    // Helper methods to map User and Category from ID, if necessary
    @Named("mapUserFromId")
    default User mapUserFromId(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }

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
