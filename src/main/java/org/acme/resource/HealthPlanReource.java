package org.acme.resource;

import java.util.List;

import org.acme.dto.ApiResponseDTO;
import org.acme.model.HealthPlan;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/health-plan")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Health Plan", description = "Health Plan related operations")
public class HealthPlanReource {

    /**
     * Retrieves a list of all health plans.
     *
     * @return a Response object containing a list of all HealthPlan entities
     *         wrapped in a successful ApiResponse.
     */
    @GET
    public Response listAll() {
        List<HealthPlan> plan = HealthPlan.listAll();
        return Response.ok(ApiResponseDTO.success(plan)).build();
    }

    /**
     * Creates a new HealthPlan resource.
     *
     * @param plan The HealthPlan object to be created. Must not be null.
     * @return A Response object containing:
     *         - HTTP 400 status if the provided HealthPlan is null, with an error
     *         message.
     *         - HTTP 200 status if the HealthPlan is successfully created, with the
     *         created object.
     */
    @POST
    @Transactional
    public Response create(HealthPlan plan) {
        if (plan == null) {
            throw new NotFoundException("Health plan cannot be null");
        }

        plan.persist();
        return Response.ok(ApiResponseDTO.success(plan)).build();
    }

    /**
     * Retrieves a health plan by its ID.
     *
     * @param id The ID of the health plan to retrieve.
     * @return A Response containing the health plan if found, or a 404 status with
     *         an error message if not found.
     */
    @GET
    @Path("{id}")
    public Response get(@PathParam("id") Long id) {
        HealthPlan plan = HealthPlan.findById(id);

        if (plan == null) {
            throw new NotFoundException("Health plan not found with ID: " + id);
        }

        return Response.ok(ApiResponseDTO.success(plan)).build();
    }

    /**
     * Updates an existing HealthPlan with the provided data.
     *
     * @param id   The ID of the HealthPlan to update.
     * @param plan The HealthPlan object containing the updated fields.
     * @return A Response object:
     *         - 200 (OK) with the updated HealthPlan if the update is successful.
     *         - 404 (Not Found) if the HealthPlan with the given ID does not exist.
     *         - 500 (Internal Server Error) if an error occurs during the update
     *         process.
     */
    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, HealthPlan plan) {
        HealthPlan existingPlan = HealthPlan.findById(id);

        if (existingPlan == null) {
            throw new NotFoundException("Health plan not found with ID: " + id);
        }

        for (var field : plan.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(plan);
                if (value != null) {
                    field.set(existingPlan, value);
                }
            } catch (IllegalAccessException e) {
                throw new WebApplicationException("Error updating Health plan field: " + field.getName(), e);
            }
        }

        existingPlan.persist();
        return Response.ok(ApiResponseDTO.success(existingPlan)).build();
    }

    /**
     * Deletes a health plan by its ID.
     *
     * @param id The ID of the health plan to be deleted.
     * @return A Response indicating the outcome of the operation:
     *         - 404 if the health plan is not found.
     *         - 200 if the health plan is successfully deleted.
     */
    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        HealthPlan plan = HealthPlan.findById(id);

        if (plan == null) {
            throw new NotFoundException("Health plan not found with ID: " + id);
        }

        HealthPlan.deleteById(id);
        return Response.ok(ApiResponseDTO.success("Health plan deleted successfully")).build();
    }
}
