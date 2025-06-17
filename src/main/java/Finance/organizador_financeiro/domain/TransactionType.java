package Finance.organizador_financeiro.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TransactionType {
    @Id
    private Long id;
    private String name;
}