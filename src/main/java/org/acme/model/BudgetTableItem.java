package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type", discriminatorType = DiscriminatorType.STRING)
public abstract class BudgetTableItem extends PanacheEntity {
    public String name;
    @Column(precision = 10, scale = 2)
    public BigDecimal basePrice;

    public BudgetTableItem() {
        // Default constructor
    }

    public BudgetTableItem(String name, BigDecimal basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }

    @Transient
    public String getItemType() {
        DiscriminatorValue val = this.getClass().getAnnotation(DiscriminatorValue.class);
        return (val != null) ? val.value() : null;
    }
}