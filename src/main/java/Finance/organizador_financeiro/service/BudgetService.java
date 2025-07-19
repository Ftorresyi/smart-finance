package Finance.organizador_financeiro.service;

import Finance.organizador_financeiro.domain.Budget;
import Finance.organizador_financeiro.domain.Category;
import Finance.organizador_financeiro.domain.User;
import Finance.organizador_financeiro.dto.BudgetDTO;
import Finance.organizador_financeiro.mapper.BudgetMapper;
import Finance.organizador_financeiro.repository.BudgetRepository;
import Finance.organizador_financeiro.repository.CategoryRepository;
import Finance.organizador_financeiro.security.AuthenticationComponent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final CategoryRepository categoryRepository;
    private final AuthenticationComponent authenticationComponent;

    public BudgetService(BudgetRepository budgetRepository, BudgetMapper budgetMapper, CategoryRepository categoryRepository, AuthenticationComponent authenticationComponent) {
        this.budgetRepository = budgetRepository;
        this.budgetMapper = budgetMapper;
        this.categoryRepository = categoryRepository;
        this.authenticationComponent = authenticationComponent;
    }

    @Transactional
    public BudgetDTO createBudget(BudgetDTO budgetDTO) {
        User user = authenticationComponent.getAuthenticatedUser();

        // Validate and fetch Category
        Category category = categoryRepository.findByIdAndUser(budgetDTO.getCategoryId(), user)
                .orElseThrow(() -> new IllegalArgumentException("Category not found or does not belong to the user."));

        // Check for existing budget for the same category and period for this user
        if (budgetRepository.findByUserAndCategoryAndPeriod(user, category, YearMonth.parse(budgetDTO.getPeriod())).isPresent()) {
            throw new IllegalStateException("Budget already exists for this category and period.");
        }

        Budget budget = budgetMapper.toEntity(budgetDTO);
        budget.setUser(user); // Set the authenticated user
        budget.setCategory(category); // Set the fetched category

        Budget savedBudget = budgetRepository.save(budget);
        return budgetMapper.toDTO(savedBudget);
    }

    @Transactional(readOnly = true)
    public List<BudgetDTO> getAllBudgets() {
        User user = authenticationComponent.getAuthenticatedUser();
        return budgetRepository.findByUser(user).stream()
                .map(budgetMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<BudgetDTO> getBudgetById(Long id) {
        User user = authenticationComponent.getAuthenticatedUser();
        return budgetRepository.findByIdAndUser(id, user)
                .map(budgetMapper::toDTO);
    }

    @Transactional
    public BudgetDTO updateBudget(Long id, BudgetDTO budgetDTO) {
        User user = authenticationComponent.getAuthenticatedUser();

        return budgetRepository.findByIdAndUser(id, user).map(existingBudget -> {
            // Validate and fetch Category if changed
            Category category = existingBudget.getCategory(); // Default to existing category
            if (budgetDTO.getCategoryId() != null && !budgetDTO.getCategoryId().equals(category.getId())) {
                category = categoryRepository.findByIdAndUser(budgetDTO.getCategoryId(), user)
                        .orElseThrow(() -> new IllegalArgumentException("New category not found or does not belong to the user."));
            }

            // Check for existing budget for the same category and period if category or period changed
            YearMonth newPeriod = YearMonth.parse(budgetDTO.getPeriod());
            if (!category.equals(existingBudget.getCategory()) || !newPeriod.equals(existingBudget.getPeriod())) {
                if (budgetRepository.findByUserAndCategoryAndPeriod(user, category, newPeriod).isPresent()) {
                    throw new IllegalStateException("Budget already exists for this category and period.");
                }
            }

            existingBudget.setAmount(budgetDTO.getAmount());
            existingBudget.setPeriod(newPeriod);
            existingBudget.setCategory(category);

            Budget updatedBudget = budgetRepository.save(existingBudget);
            return budgetMapper.toDTO(updatedBudget);
        }).orElseThrow(() -> new IllegalArgumentException("Budget not found or does not belong to the user."));
    }

    @Transactional
    public boolean deleteBudget(Long id) {
        User user = authenticationComponent.getAuthenticatedUser();
        return budgetRepository.findByIdAndUser(id, user).map(budget -> {
            budgetRepository.delete(budget);
            return true;
        }).orElse(false);
    }

    @Transactional(readOnly = true)
    public List<BudgetDTO> getBudgetsForPeriod(int year, int month) {
        User user = authenticationComponent.getAuthenticatedUser();
        YearMonth period = YearMonth.of(year, month);
        return budgetRepository.findByUserAndPeriod(user, period).stream()
                .map(budgetMapper::toDTO)
                .collect(Collectors.toList());
    }
}
