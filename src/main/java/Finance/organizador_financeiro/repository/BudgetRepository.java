package Finance.organizador_financeiro.repository;

import Finance.organizador_financeiro.domain.Budget;
import Finance.organizador_financeiro.domain.Category;
import Finance.organizador_financeiro.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUser(User user);
    Optional<Budget> findByIdAndUser(Long id, User user);
    Optional<Budget> findByUserAndCategoryAndPeriod(User user, Category category, YearMonth period);
    List<Budget> findByUserAndPeriod(User user, YearMonth period);
}
