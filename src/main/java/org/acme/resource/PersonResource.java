package org.acme.resource;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.acme.dto.ApiResponseDTO;
import org.acme.model.Person;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/people")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Person", description = "Person related operations")
public class PersonResource {

    /**
     * Retrieves a list of all Person entities.
     *
     * @return a Response containing a successful ApiResponse with the list of all
     *         Person entities.
     */
    @GET
    public Response listAll() {
        List<Person> people = Person.listAll();
        return Response.ok(ApiResponseDTO.success(people)).build();
    }

    /**
     * Creates a new Person entity and persists it to the database.
     *
     * @param person The Person object to be created. Must not be null.
     * @return A Response object containing:
     *         - HTTP 200 status with the created Person object if successful.
     *         - HTTP 400 status with an error message if the Person object is null.
     */
    @POST
    @Transactional
    public Response create(Person person) {
        if (person == null) {
            throw new NotFoundException("Person cannot be null");
        }

        person.persist();
        return Response.ok(ApiResponseDTO.success(person)).build();
    }

    /**
     * Retrieves a person by their ID.
     *
     * @param id the ID of the person to retrieve
     * @return a Response containing the person data if found, or a 404 status with
     *         an error message if not found
     */
    @GET
    @Path("{id}")
    public Response get(@PathParam("id") Long id) {
        Person person = Person.findById(id);

        if (person == null) {
            throw new NotFoundException("Person not found with ID: " + id);
        }

        return Response.ok(ApiResponseDTO.success(person)).build();
    }

    /**
     * Updates an existing person with the provided data.
     * 
     * @param id     The ID of the person to update.
     * @param person The person object containing the updated data.
     * @return A Response object:
     *         - 200 OK with the updated person if the update is successful.
     *         - 404 Not Found if the person with the given ID does not exist.
     *         - 500 Internal Server Error if an error occurs during the update
     *         process.
     */
    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, Person person) {
        Person existingPerson = Person.findById(id);

        if (existingPerson == null) {
            throw new NotFoundException("Person not found with ID: " + id);
        }

        for (var field : person.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(person);
                if (value != null) {
                    field.set(existingPerson, value);
                }
            } catch (IllegalAccessException e) {
                throw new WebApplicationException("Error updating person field: " + field.getName(), e);
            }
        }

        existingPerson.persist();
        return Response.ok(ApiResponseDTO.success(existingPerson)).build();
    }

    /**
     * Deletes a person by their ID.
     *
     * @param id the ID of the person to be deleted
     * @return a Response indicating the outcome of the delete operation:
     *         - 404 if the person is not found
     *         - 200 if the person is successfully deleted
     */
    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Person person = Person.findById(id);

        if (person == null) {
            throw new NotFoundException("Person not found with ID: " + id);
        }

        Person.deleteById(id);
        return Response.ok(ApiResponseDTO.success("Person deleted successfully")).build();
    }
}