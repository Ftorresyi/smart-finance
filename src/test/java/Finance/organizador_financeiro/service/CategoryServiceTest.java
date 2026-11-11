package Finance.organizador_financeiro.service;

import Finance.organizador_financeiro.domain.Category;
import Finance.organizador_financeiro.domain.User;
import Finance.organizador_financeiro.dto.CategoryDTO;
import Finance.organizador_financeiro.mapper.CategoryMapper;
import Finance.organizador_financeiro.repository.CategoryRepository;
import Finance.organizador_financeiro.security.AuthenticationComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoryMapper mapper;

    @Mock
    private AuthenticationComponent authenticationComponent;

    @InjectMocks
    private CategoryService service;

    private User authenticatedUser;

    @BeforeEach
    void setUp() {
        authenticatedUser = new User();
        authenticatedUser.setId(7L);
        when(authenticationComponent.getAuthenticatedUser()).thenReturn(authenticatedUser);
    }

    @Test
    void findAllReturnsOnlyCategoriesForAuthenticatedUser() {
        Category category = new Category();
        CategoryDTO dto = new CategoryDTO(1L, "Food", "Meals");
        when(repository.findByUser(authenticatedUser)).thenReturn(List.of(category));
        when(mapper.toDTO(category)).thenReturn(dto);

        assertEquals(List.of(dto), service.findAll());
        verify(repository).findByUser(authenticatedUser);
    }

    @Test
    void createAssociatesCategoryWithAuthenticatedUser() {
        CategoryDTO input = new CategoryDTO(null, "Food", "Meals");
        Category category = new Category();
        CategoryDTO savedDto = new CategoryDTO(1L, "Food", "Meals");
        when(mapper.toEntity(input)).thenReturn(category);
        when(repository.save(category)).thenReturn(category);
        when(mapper.toDTO(category)).thenReturn(savedDto);

        assertSame(savedDto, service.create(input));
        assertSame(authenticatedUser, category.getUser());
        verify(repository).save(category);
    }

    @Test
    void updateKeepsCategoryOwnedByAuthenticatedUser() {
        long categoryId = 3L;
        Category existing = new Category();
        Category updated = new Category();
        CategoryDTO input = new CategoryDTO(categoryId, "Food", "Meals");
        when(repository.findByIdAndUser(categoryId, authenticatedUser)).thenReturn(Optional.of(existing));
        when(mapper.toEntity(input)).thenReturn(updated);
        when(repository.save(updated)).thenReturn(updated);
        when(mapper.toDTO(updated)).thenReturn(input);

        assertTrue(service.update(categoryId, input).isPresent());

        ArgumentCaptor<Category> savedCategory = ArgumentCaptor.forClass(Category.class);
        verify(repository).save(savedCategory.capture());
        assertEquals(categoryId, savedCategory.getValue().getId());
        assertSame(authenticatedUser, savedCategory.getValue().getUser());
    }

    @Test
    void updateDoesNotSaveCategoryOwnedByAnotherUser() {
        long categoryId = 3L;
        when(repository.findByIdAndUser(categoryId, authenticatedUser)).thenReturn(Optional.empty());

        assertTrue(service.update(categoryId, new CategoryDTO()).isEmpty());
        verify(repository, never()).save(any());
    }

    @Test
    void deleteDoesNotDeleteCategoryOwnedByAnotherUser() {
        long categoryId = 3L;
        when(repository.findByIdAndUser(categoryId, authenticatedUser)).thenReturn(Optional.empty());

        assertFalse(service.delete(categoryId));
        verify(repository, never()).delete(any());
    }
}
