package org.acme.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BudgetDTO {
    public Long id;
    public LocalDate creationDate;
    public LocalDate validityDate;
    public PersonDTO person;
    public HealthPlanDTO healthPlan;
    public BigDecimal grossTotalAmount = BigDecimal.ZERO;
    public BigDecimal totalDiscountAmount = BigDecimal.ZERO;
    public BigDecimal netTotalAmount = BigDecimal.ZERO;

    public List<BudgetItemDTO> items;
}
