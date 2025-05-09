package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class BudgetItem extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id")
    @JsonBackReference
    public Budget budget;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "budgetableitem_id")
    public BudgetTableItem item;

    public int quantity;

    @Column(precision = 10, scale = 2)
    public BigDecimal grossUnitPrice;

    @Column(precision = 10, scale = 2)
    public BigDecimal itemDiscountAmount = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    public BigDecimal netUnitPrice;

    @Column(precision = 10, scale = 2)
    public BigDecimal netTotalItemAmount;

    /**
     * Calculates and updates the financial values for the budget item.
     * <p>
     * This method ensures that the gross unit price, item discount amount, net unit
     * price,
     * and net total item amount are properly calculated and set based on the
     * current state
     * of the object. It handles null values and ensures that the net unit price
     * does not
     * fall below zero.
     * <ul>
     * <li>If {@code grossUnitPrice} is null, it is set to the base price of the
     * item or
     * {@code BigDecimal.ZERO} if the item is null.</li>
     * <li>If {@code itemDiscountAmount} is null, it is initialized to
     * {@code BigDecimal.ZERO}.</li>
     * <li>The {@code netUnitPrice} is calculated as the difference between
     * {@code grossUnitPrice}
     * and {@code itemDiscountAmount}. If the result is negative,
     * {@code netUnitPrice} is set
     * to {@code BigDecimal.ZERO}, and {@code itemDiscountAmount} is adjusted to
     * equal
     * {@code grossUnitPrice}.</li>
     * <li>The {@code netTotalItemAmount} is calculated as the product of
     * {@code netUnitPrice}
     * and {@code quantity}, rounded to two decimal places using
     * {@code RoundingMode.HALF_UP}.</li>
     * </ul>
     */
    public void calculateValues() {
        if (this.grossUnitPrice == null) {
            this.grossUnitPrice = this.item != null ? this.item.basePrice : BigDecimal.ZERO;
        }
        if (this.itemDiscountAmount == null) {
            this.itemDiscountAmount = BigDecimal.ZERO;
        }

        this.netUnitPrice = this.grossUnitPrice.subtract(this.itemDiscountAmount);
        if (this.netUnitPrice.compareTo(BigDecimal.ZERO) < 0) {
            this.netUnitPrice = BigDecimal.ZERO;
            this.itemDiscountAmount = this.grossUnitPrice;
        }

        this.netTotalItemAmount = this.netUnitPrice.multiply(BigDecimal.valueOf(this.quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }
}
