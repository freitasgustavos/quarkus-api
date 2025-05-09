package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Person extends PanacheEntity {
    public String name;
    @Column(unique = true)
    public String documentNumber;
    public LocalDate birthDate;
    public String phone;
    public String email;

    public Person() {
        // Default constructor
    }

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }
}