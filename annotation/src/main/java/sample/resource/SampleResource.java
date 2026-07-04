package sample.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import sample.application.SampleApplication;

@Path("/sample")
@Produces(MediaType.APPLICATION_JSON)
public class SampleResource {

    @Inject
    SampleApplication sampleApplication;

    @GET
    public SampleResponse hello() {
        String message =sampleApplication.getMessage();
        
        return new SampleResponse(message);
    }

    public static record SampleResponse(String message) {
    }
}
