package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.math.RoundingMode;

@Entity
public class Budget extends PanacheEntity {
    public LocalDate creationDate;
    public LocalDate validityDate;

    @Column(precision = 12, scale = 2)
    public BigDecimal grossTotalAmount = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    public BigDecimal totalDiscountAmount = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    public BigDecimal netTotalAmount = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    public Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "healthplan_id")
    public HealthPlan healthPlan;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    public List<BudgetItem> items = new ArrayList<>();

    @PrePersist
    void prePersist() {
        creationDate = LocalDate.now();

        if (validityDate == null) {
            validityDate = creationDate.plusDays(30);
        }
    }

    /**
     * Recalculates the total amounts for the budget based on its items.
     * 
     * <p>
     * This method iterates through all the items in the budget, calculates their
     * individual values, and updates the following totals:
     * <ul>
     * <li><b>grossTotalAmount</b>: The sum of the gross unit price of each item
     * multiplied by its quantity.</li>
     * <li><b>totalDiscountAmount</b>: The sum of the discount amount of each item
     * multiplied by its quantity.</li>
     * <li><b>netTotalAmount</b>: The sum of the net total amount of each item.</li>
     * </ul>
     * 
     * <p>
     * All totals are rounded to two decimal places using
     * {@link RoundingMode#HALF_UP}.
     */
    public void recalculateTotals() {
        this.grossTotalAmount = BigDecimal.ZERO;
        this.totalDiscountAmount = BigDecimal.ZERO;
        this.netTotalAmount = BigDecimal.ZERO;

        for (BudgetItem item : items) {
            item.calculateValues();
            this.grossTotalAmount = this.grossTotalAmount
                    .add(item.grossUnitPrice.multiply(BigDecimal.valueOf(item.quantity)));
            this.totalDiscountAmount = this.totalDiscountAmount
                    .add(item.itemDiscountAmount.multiply(BigDecimal.valueOf(item.quantity)));
            this.netTotalAmount = this.netTotalAmount.add(item.netTotalItemAmount);
        }

        this.grossTotalAmount = this.grossTotalAmount.setScale(2, RoundingMode.HALF_UP);
        this.totalDiscountAmount = this.totalDiscountAmount.setScale(2, RoundingMode.HALF_UP);
        this.netTotalAmount = this.netTotalAmount.setScale(2, RoundingMode.HALF_UP);
    }
}
