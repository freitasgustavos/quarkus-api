package org.acme.mapper;

import org.acme.dto.HealthPlanDTO;
import org.acme.model.HealthPlan;

public class HealthPlanMapper {
    public static HealthPlanDTO toDTO(HealthPlan healthPlan) {
        if (healthPlan == null) {
            return null;
        }
        
        HealthPlanDTO dto = new HealthPlanDTO();
        dto.id = healthPlan.id;
        dto.name = healthPlan.name;
        dto.ansRegistry = healthPlan.ansRegistry;

        return dto;
    }
}
