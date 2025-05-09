package org.acme.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("FEE")
public class Fee extends BudgetTableItem {
    public String description;

    public Fee() {
        // Default constructor
    }

    public Fee(String description, BigDecimal basePrice, String name) {
        this.name = name;
        this.basePrice = basePrice;
        this.description = description;
    }
}
