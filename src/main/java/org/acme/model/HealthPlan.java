package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
public class HealthPlan extends PanacheEntity {
    public String name;
    public String ansRegistry;

    public HealthPlan() {
        // Default constructor
    }

    public HealthPlan(String name, String ansRegistry) {
        this.name = name;
        this.ansRegistry = ansRegistry;
    }
}