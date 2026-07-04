package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/sample")
@Produces(MediaType.APPLICATION_JSON)
public class SampleResource {

    @GET
    public SampleResponse hello() {
        return new SampleResponse("Hello world");
    }

    public static record SampleResponse(String message) {
    }
}
