package xyz.property.data.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Slf4j
public class CircuitBreakerExceptionHandler implements ExceptionMapper<CircuitBreakerOpenException> {

    @Override
    public Response toResponse(CircuitBreakerOpenException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Circuit breaker is open. Please check the system logs.").build();
    }
}
