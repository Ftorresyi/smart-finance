package Finance.organizador_financeiro.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public class BudgetDTO {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private BigDecimal amount;
    private String period;

    public BudgetDTO() {
    }

    public BudgetDTO(Long id, Long categoryId, String categoryName, BigDecimal amount, String period) {
        this.id = id;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.amount = amount;
        this.period = period;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
