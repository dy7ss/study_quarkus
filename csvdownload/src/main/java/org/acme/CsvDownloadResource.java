package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class CsvDownloadResource {

    @GET
    @Path("/csv")
    @Produces("text/csv")
    public Response downloadCsv() {
        String csv = "id,name,age\n" +
                "1,Alice,30\n" +
                "2,Bob,25\n";

        return Response.ok(csv)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"sample.csv\"")
                .type(MediaType.valueOf("text/csv; charset=UTF-8"))
                .build();
    }
}
