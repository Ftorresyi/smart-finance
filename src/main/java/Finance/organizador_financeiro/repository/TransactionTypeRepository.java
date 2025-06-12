package Finance.organizador_financeiro.repository;

import Finance.organizador_financeiro.domain.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionTypeRepository extends JpaRepository<TransactionType, Integer> {
}
