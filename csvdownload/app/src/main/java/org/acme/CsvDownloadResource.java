package org.acme;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;

@Path("/")
public class CsvDownloadResource {

    @Inject
    EntityManager entityManager;

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

    @GET
    @Path("/csv/timesleep")
    @Produces("text/csv")
    public Response stream() {
        // ストリーミングレスポンスを行う
        // $curl http://localhost:8080/csv/timesleep
        StreamingOutput output = outputStream -> {
            for (int i = 1; i <= 10; i++) {
                outputStream.write(("data-" + i + "\n").getBytes());
                outputStream.flush();
                try {
                    System.out.println("1000ms sleep");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IOException(e);
                }
            }
        };
        return Response.ok(output).build();
    }

    @GET
    @Path("/csv/many")
    @Produces("text/csv")
    public Response downloadManyCsv(@QueryParam("branchNo") Integer branchNo,
            @QueryParam("streaming") String streaming) {
        // curl http://localhost:8080/csv/many?streaming=true
        String jpql = "SELECT c FROM Customer c WHERE 1 = 1";
        if (branchNo != null) {
            jpql += " AND c.branchNo = :branchNo";
        }
        jpql += " ORDER BY c.customerId ASC";

        var query = entityManager.createQuery(jpql, Customer.class);
        if (branchNo != null) {
            query.setParameter("branchNo", branchNo);
        }

        List<Customer> customers = query
                // .setMaxResults(5)
                .getResultList();

        String csv = "customer_id,customer_name,branch_no\n" +
                customers.stream()
                        .map(customer -> customer.getCustomerId() + "," + customer.getCustomerName() + ","
                                + customer.getBranchNo())
                        .collect(Collectors.joining("\n"));

        if (!customers.isEmpty()) {
            csv += "\n";
        }

        boolean useStreaming = "true".equalsIgnoreCase(streaming);
        if (useStreaming) {
            // ストリーミングレスポンスを行う想定だが、DBの読み込みが律速となり、HTTPレスポンスを一度に返している？
            StreamingOutput streamingOutput = outputStream -> {
                try (var writer = new java.io.PrintWriter(
                        new java.io.OutputStreamWriter(outputStream, java.nio.charset.StandardCharsets.UTF_8))) {
                    writer.println("customer_id,customer_name,branch_no");
                    for (Customer customer : customers) {
                        writer.printf("%d,%s,%d%n",
                                customer.getCustomerId(),
                                customer.getCustomerName(),
                                customer.getBranchNo());
                    }
                }
            };

            return Response.ok(streamingOutput)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"customers.csv\"")
                    .type(MediaType.valueOf("text/csv; charset=UTF-8"))
                    .build();
        }

        return Response.ok(csv)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"customers.csv\"")
                .type(MediaType.valueOf("text/csv; charset=UTF-8"))
                .build();
    }
}
