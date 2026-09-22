package restapi.controller;

import java.net.URI;

import application.FeedApplicationService;
import application.dto.RegisterFeedCommand;
import domain.entity.Feed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import restapi.model.request.CreateFeedRequest;
import restapi.model.response.FeedPageResponse;
import restapi.model.response.FeedResponse;

@Path("/feeds")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class FeedResource {

    @Inject
    FeedApplicationService service;

    @POST
    public Response register(CreateFeedRequest request) {
        Feed feed = service.register(new RegisterFeedCommand(request.feedUrl()));
        return Response.created(URI.create("/feeds/" + feed.id())).entity(FeedResponse.from(feed)).build();
    }

    @GET
    public FeedPageResponse list(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
        int actualPage = page == null ? 0 : page;
        int actualSize = size == null ? 20 : size;
        var result = service.list(actualPage, actualSize);
        return new FeedPageResponse(result.items().stream().map(FeedResponse::from).toList(), result.page(),
                result.size(), result.total());
    }

    @GET
    @Path("/{feedId}")
    public Response get(@PathParam("feedId") long feedId) {
        return Response.ok(FeedResponse.from(service.get(feedId))).build();
    }

    @DELETE
    @Path("/{feedId}")
    public Response delete(@PathParam("feedId") long feedId) {
        service.delete(feedId);
        return Response.noContent().build();
    }
}