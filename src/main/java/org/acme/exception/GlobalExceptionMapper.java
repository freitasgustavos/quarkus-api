package org.acme.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.dto.ApiResponseDTO;
import jakarta.ws.rs.WebApplicationException;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof WebApplicationException) {
            WebApplicationException webEx = (WebApplicationException) exception;
            return Response.status(webEx.getResponse().getStatus())
                    .entity(ApiResponseDTO.error(webEx.getMessage()))
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponseDTO.error("An unexpected error occurred: " + exception.getMessage()))
                .build();
    }
}