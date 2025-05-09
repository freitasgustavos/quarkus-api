package org.acme.service;

import org.acme.dto.BudgetInputDTO;
import org.acme.dto.BudgetItemInputDTO;
import org.acme.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BudgetService {

    @Inject
    EntityManager entityManager;

    /**
     * Creates a new budget based on the provided input.
     *
     * @param input The {@link BudgetInputDTO} containing the details for the budget
     *              creation.
     *              It includes the person ID, health plan ID, validity date, and a
     *              list of budget items.
     * @return The newly created {@link Budget} entity.
     * @throws NotFoundException       If the person, health plan, or any budgetable
     *                                 item specified in the input is not found.
     * @throws WebApplicationException If the discount for any budget item exceeds
     *                                 its gross unit price.
     */
    @Transactional
    public Budget createBudget(BudgetInputDTO input) {
        Person person = Person.<Person>findByIdOptional(input.personId)
                .orElseThrow(() -> new NotFoundException("Person not found with ID: " + input.personId));

        HealthPlan healthPlan = HealthPlan.<HealthPlan>findByIdOptional(input.healthPlanId)
                .orElseThrow(() -> new NotFoundException("HealthPlan not found with ID: " + input.healthPlanId));

        Budget budget = new Budget();
        budget.person = person;
        budget.healthPlan = healthPlan;
        budget.validityDate = input.validityDate;

        for (BudgetItemInputDTO itemInput : input.items) {
            BudgetTableItem baseItem = BudgetTableItem.<BudgetTableItem>findByIdOptional(itemInput.budgetableItemId)
                    .orElseThrow(() -> new NotFoundException(
                            "BudgetableItem not found with ID: " + itemInput.budgetableItemId));

            BudgetItem budgetItem = new BudgetItem();
            budgetItem.setBudget(budget);
            budgetItem.item = baseItem;
            budgetItem.quantity = itemInput.quantity;

            budgetItem.grossUnitPrice = baseItem.basePrice;

            // Recebe o percentual do desconto
            BigDecimal discountPercent = Optional.ofNullable(itemInput.itemDiscountAmount).orElse(BigDecimal.ZERO);

            // Calcula o valor do desconto em reais
            BigDecimal discountValue = budgetItem.grossUnitPrice
                    .multiply(discountPercent)
                    .divide(BigDecimal.valueOf(100));

            // Calcula o preço líquido
            budgetItem.netUnitPrice = budgetItem.grossUnitPrice.subtract(discountValue);

            budgetItem.itemDiscountAmount = discountValue;
            budgetItem.netUnitPrice = budgetItem.grossUnitPrice.subtract(discountValue);

            if (budgetItem.itemDiscountAmount.compareTo(budgetItem.grossUnitPrice) > 0) {
                throw new WebApplicationException("Discount for item " + baseItem.name + " ("
                        + budgetItem.itemDiscountAmount + ") cannot exceed the gross unit price ("
                        + budgetItem.grossUnitPrice + ")", Response.Status.BAD_REQUEST);
            }

            budget.items.add(budgetItem);
        }

        budget.recalculateTotals();

        budget.persist();

        return budget;
    }

    /**
     * Retrieves a list of all budgets.
     *
     * @return a list containing all Budget entities.
     */
    public List<Budget> listAllBudgets() {
        return Budget.listAll();
    }

    /**
     * Finds a Budget entity by its unique identifier.
     *
     * @param id the unique identifier of the Budget entity to be retrieved
     * @return an Optional containing the Budget entity if found, or an empty
     *         Optional if not found
     */
    public Optional<Budget> findBudgetById(Long id) {
        return Budget.findByIdOptional(id);
    }
}