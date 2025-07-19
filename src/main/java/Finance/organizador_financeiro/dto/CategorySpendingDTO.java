package Finance.organizador_financeiro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySpendingDTO {
    private String categoryName;
    private BigDecimal totalSpent;
}
