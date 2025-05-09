package org.acme.seed;

import org.acme.model.Person;
import org.acme.model.Exam;
import org.acme.model.Fee;
import org.acme.model.HealthPlan;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class DataSeeder {

    @Transactional
    public void seed() {
        seedHealthPlans();
        seedPersons();
        seedBudgeTableItems();
    }

    private void seedHealthPlans() {
        if (HealthPlan.count() == 0) {
            List.of(
                    new HealthPlan("Unimed", "416054991"))
                    .forEach(healthPlan -> healthPlan.persist());
        }
    }

    private void seedPersons() {
        if (Person.count() == 0) {
            List.of(
                    new Person("John Doe", "john.doe@example.com")).forEach(person -> person.persist());
        }
    }

    private void seedBudgeTableItems() {
        if (Fee.count() == 0 && Exam.count() == 0) {
            Fee fee = new Fee("Taxa de coleta", new BigDecimal("15.00"), "Taxa");
            fee.persist();

            List.of(
                    new Exam("TUSS123", "Jejum de 8h", new BigDecimal("210.50"), "Exame de Sangue"),
                    new Exam("TUSS456", "Jejum de 6h", new BigDecimal("89.90"), "Exame de Covid"),
                    new Exam("TUSS789", "Jejum de 4h", new BigDecimal("549.00"), "Exame de DNA"))
                    .forEach(exam -> exam.persist());
        }
    }

}