package Finance.organizador_financeiro.repository;

import Finance.organizador_financeiro.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}