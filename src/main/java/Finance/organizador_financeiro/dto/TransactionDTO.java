package Finance.organizador_financeiro.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private Long userId;
    private Long categoryId;
    private String type; // INCOME ou EXPENSE

}