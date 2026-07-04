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
        String message = sampleApplication.getMessage();
        System.out.println(message);

        String message2 = sampleApplication.getMessage("type-value1", "type-value2");
        System.out.println(message2);
        
        return new SampleResponse(message);
    }

    public static record SampleResponse(String message) {
    }
}
