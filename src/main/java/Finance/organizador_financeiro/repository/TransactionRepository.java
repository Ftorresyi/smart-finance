package Finance.organizador_financeiro.repository;

import Finance.organizador_financeiro.domain.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
