package org.acme.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class BudgetInputDTO {
    @NotNull(message = "Person ID is required")
    public Long personId;

    @NotNull(message = "Health Plan ID is required")
    public Long healthPlanId;

    @FutureOrPresent(message = "Validity date must be in the present or future")
    public LocalDate validityDate;

    @NotEmpty(message = "Budget must have at least one item")
    @Valid
    public List<BudgetItemInputDTO> items;
}