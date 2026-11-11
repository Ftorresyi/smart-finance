package Finance.organizador_financeiro.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    @JsonAlias("transactionDate")
    private LocalDate date;

    @NotBlank
    private String description;
    private Long userId;

    @NotNull
    @Positive
    private Long categoryId;

    @NotNull
    @Pattern(regexp = "(?i)INCOME|EXPENSE")
    private String type;
}
