package org.acme.mapper;

import org.acme.dto.BudgetTableItemDTO;
import org.acme.model.BudgetTableItem;
import org.acme.model.Exam;
import org.acme.model.Fee;

public class BudgetTableItemMapper {

    public static BudgetTableItemDTO toDTO(BudgetTableItem item) {
        if (item == null) {
            return null;
        }

        BudgetTableItemDTO dto = new BudgetTableItemDTO();
        dto.id = item.id;
        dto.name = item.name;
        dto.basePrice = item.basePrice;
        dto.tussCode = item instanceof Exam ? ((Exam) item).tussCode : null;
        dto.preparationInstructions = item instanceof Exam ? ((Exam) item).preparationInstructions : null;
        dto.description = item instanceof Fee ? ((Fee) item).description : null;
        dto.itemType = item instanceof Exam ? "EXAM" : "FEE";

        return dto;
    }
}
