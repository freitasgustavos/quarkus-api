package org.acme.mapper;

import java.util.stream.Collectors;

import org.acme.dto.BudgetDTO;
import org.acme.dto.BudgetItemDTO;
import org.acme.model.Budget;
import org.acme.model.BudgetItem;

public class BudgetMapper {
    public static BudgetDTO toDTO(Budget budget) {
        if (budget == null) {
            return null;
        }

        BudgetDTO dto = new BudgetDTO();

        dto.id = budget.id;
        dto.person = budget.person != null ? PersonMapper.toDTO(budget.person) : null;
        dto.healthPlan = budget.healthPlan != null ? HealthPlanMapper.toDTO(budget.healthPlan) : null;
        dto.creationDate = budget.creationDate;
        dto.validityDate = budget.validityDate;
        dto.grossTotalAmount = budget.grossTotalAmount;
        dto.totalDiscountAmount = budget.totalDiscountAmount;
        dto.netTotalAmount = budget.netTotalAmount;

        dto.items = budget.items.stream()
                .map(BudgetMapper::toItemDTO)
                .collect(Collectors.toList());
        return dto;
    }

    public static BudgetItemDTO toItemDTO(BudgetItem item) {
        if (item == null) {
            return null;
        }

        BudgetItemDTO dto = new BudgetItemDTO();
        dto.id = item.id;
        dto.itemDiscountAmount = item.itemDiscountAmount;
        dto.grossUnitPrice = item.grossUnitPrice;
        dto.netUnitPrice = item.netUnitPrice;
        dto.quantity = item.quantity;
        dto.item = BudgetTableItemMapper.toDTO(item.item);
        return dto;
    }
}
