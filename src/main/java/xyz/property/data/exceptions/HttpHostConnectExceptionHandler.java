package xyz.property.data.exceptions;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class HttpHostConnectExceptionHandler implements ExceptionMapper<InternalServerErrorException> {
    @Override
    public Response toResponse(InternalServerErrorException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Impossible to establish a connection with the data provider.")
                .build();
    }
}
