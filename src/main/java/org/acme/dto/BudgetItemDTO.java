package org.acme.dto;

import java.math.BigDecimal;

public class BudgetItemDTO {
    public Long id;
    public BigDecimal grossUnitPrice = BigDecimal.ZERO;
    public BigDecimal itemDiscountAmount = BigDecimal.ZERO;
    public BigDecimal netUnitPrice = BigDecimal.ZERO;
    public Integer quantity;
    public BudgetTableItemDTO item;
}
