package Finance.organizador_financeiro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private Long userId;
    private Long categoryId;
    private String type; // INCOME ou EXPENSE
}
