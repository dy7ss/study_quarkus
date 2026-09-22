package restapi.exception;

import application.exception.FeedNotFoundException;
import application.exception.InvalidFeedPageException;
import domain.exception.DuplicateFeedException;
import domain.exception.InvalidFeedException;
import infrastructure.external.CamelFeedMetadataProvider.FeedParseException;
import infrastructure.external.CamelFeedMetadataProvider.FeedUnavailableException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import restapi.model.response.error.ErrorResponse;

@Provider
public class FeedExceptionMapper implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException exception) {
        if (exception instanceof InvalidFeedException) {
            return response(Response.Status.BAD_REQUEST, exception, "INVALID_FEED");
        }
        if (exception instanceof InvalidFeedPageException) {
            return response(Response.Status.BAD_REQUEST, exception, "INVALID_PAGE");
        }
        if (exception instanceof DuplicateFeedException) {
            return response(Response.Status.CONFLICT, exception, "FEED_ALREADY_EXISTS");
        }
        if (exception instanceof FeedNotFoundException) {
            return response(Response.Status.NOT_FOUND, exception, "FEED_NOT_FOUND");
        }
        if (exception instanceof FeedParseException) {
            return response(Response.Status.BAD_GATEWAY, exception, "RSS_PARSE_ERROR");
        }
        if (exception instanceof FeedUnavailableException) {
            return response(Response.Status.SERVICE_UNAVAILABLE, exception, "RSS_UNAVAILABLE");
        }
        return response(Response.Status.INTERNAL_SERVER_ERROR, exception, "INTERNAL_ERROR");
    }

    private Response response(Response.Status status, RuntimeException exception, String code) {
        return Response.status(status).entity(new ErrorResponse(exception.getMessage(), code)).build();
    }
}