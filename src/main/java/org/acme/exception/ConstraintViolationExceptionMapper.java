package org.acme.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.validation.ConstraintViolationException;
import org.acme.dto.ApiResponseDTO;
import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    public static class Violation {
        public String field;
        public String message;

        public Violation(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<Violation> violations = exception.getConstraintViolations().stream()
                .map(v -> new Violation(v.getPropertyPath().toString(), v.getMessage()))
                .collect(Collectors.toList());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponseDTO.error("Validation failed", violations))
                .build();
    }
}