package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/feed")
@Produces(MediaType.APPLICATION_JSON)
public class FeedResource {

    @Inject
    FeedClient feedClient;

    @GET
    public Response getFeed() {
        try {
            return Response.ok(feedClient.read()).build();
        } catch (FeedClient.FeedUnavailableException e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(new ErrorResponse("RSS feed is temporarily unavailable"))
                    .build();
        } catch (FeedClient.FeedParseException e) {
            return Response.status(Response.Status.BAD_GATEWAY)
                    .entity(new ErrorResponse("RSS feed has an invalid format"))
                    .build();
        }
    }

    public record ErrorResponse(String message) {
    }
}
