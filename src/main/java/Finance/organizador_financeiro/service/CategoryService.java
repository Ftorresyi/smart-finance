package Finance.organizador_financeiro.service;

import Finance.organizador_financeiro.domain.Category;
import Finance.organizador_financeiro.domain.User;
import Finance.organizador_financeiro.dto.CategoryDTO;
import Finance.organizador_financeiro.mapper.CategoryMapper;
import Finance.organizador_financeiro.repository.CategoryRepository;
import Finance.organizador_financeiro.security.AuthenticationComponent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final AuthenticationComponent authenticationComponent;

    public CategoryService(CategoryRepository repository, CategoryMapper mapper, AuthenticationComponent authenticationComponent) {
        this.repository = repository;
        this.mapper = mapper;
        this.authenticationComponent = authenticationComponent;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        User user = authenticationComponent.getAuthenticatedUser();
        return repository.findByUser(user).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CategoryDTO> findById(Long id) {
        User user = authenticationComponent.getAuthenticatedUser();
        return repository.findByIdAndUser(id, user)
                .map(mapper::toDTO);
    }

    @Transactional
    public CategoryDTO create(CategoryDTO dto) {
        User user = authenticationComponent.getAuthenticatedUser();
        Category entity = mapper.toEntity(dto);
        entity.setUser(user); // Associate the category with the logged-in user
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public Optional<CategoryDTO> update(Long id, CategoryDTO dto) {
        User user = authenticationComponent.getAuthenticatedUser();
        return repository.findByIdAndUser(id, user).map(existingCategory -> {
            Category updatedCategory = mapper.toEntity(dto);
            updatedCategory.setId(id); // Ensure the ID remains the same
            updatedCategory.setUser(user); // Ensure the category owner is not changed
            return mapper.toDTO(repository.save(updatedCategory));
        });
    }

    @Transactional
    public boolean delete(Long id) {
        User user = authenticationComponent.getAuthenticatedUser();
        return repository.findByIdAndUser(id, user).map(category -> {
            repository.delete(category);
            return true;
        }).orElse(false);
    }
}
