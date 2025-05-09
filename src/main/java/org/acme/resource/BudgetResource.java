package org.acme.resource;

import org.acme.dto.ApiResponseDTO;
import org.acme.dto.BudgetDTO;
import org.acme.dto.BudgetInputDTO;
import org.acme.mapper.BudgetMapper;
import org.acme.model.Budget;
import org.acme.service.BudgetService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.*;
import java.util.*;

@Path("/api/budget")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Budget", description = "Budget related operations")
public class BudgetResource {

    @Inject
    BudgetService budgetService;

    /**
     * Creates a new budget based on the provided input data.
     *
     * @param budgetInput the input data for creating the budget
     * @return a Response containing the created budget wrapped in a success API
     *         response
     */
    @POST
    public Response createBudget(@Valid BudgetInputDTO budgetInput) {

        Budget createdBudget = budgetService.createBudget(budgetInput);
        BudgetDTO dto = BudgetMapper.toDTO(createdBudget);

        return Response.ok(ApiResponseDTO.success(dto)).build();

    }

    /**
     * Retrieves a list of all budgets.
     *
     * @return a Response object containing a JSON representation of all budgets
     *         wrapped in an ApiResponse object with a success status.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBudgets() {
        List<Budget> budgets = budgetService.listAllBudgets();

        List<BudgetDTO> dtoList = budgets.stream().map(BudgetMapper::toDTO).toList();

        return Response.ok(ApiResponseDTO.success(dtoList)).build();
    }

    /**
     * Retrieves a budget by its ID.
     *
     * @param id The ID of the budget to retrieve.
     * @return A Response containing the budget if found, or a 404 status with an
     *         error message if not found.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBudgetById(@PathParam("id") Long id) {
        Optional<Budget> budgetOptional = budgetService.findBudgetById(id);

        BudgetDTO dto = budgetOptional.map(BudgetMapper::toDTO).orElse(null);

        return budgetOptional
                .map(budget -> Response.ok(ApiResponseDTO.success(dto)).build())
                .orElseThrow(() -> new NotFoundException("Budget not found with ID: " + id));
    }

}