package org.acme.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("EXAM")
public class Exam extends BudgetTableItem {
    public String tussCode;
    public String preparationInstructions;

    public Exam() {
        // Default constructor
    }

    public Exam(String tussCode, String preparationInstructions, BigDecimal basePrice, String name) {
        super(name, basePrice);
        this.tussCode = tussCode;
        this.preparationInstructions = preparationInstructions;
    }
}