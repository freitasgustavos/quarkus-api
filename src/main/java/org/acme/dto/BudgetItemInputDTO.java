package org.acme.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class BudgetItemInputDTO {
    @NotNull(message = "Budgetable Item ID is required")
    public Long budgetableItemId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    public Integer quantity;

    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    public BigDecimal itemDiscountAmount;
}
