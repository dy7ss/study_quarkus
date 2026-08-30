package org.acme;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    @Path("/csv/many")
    @Produces("text/csv")
    @Transactional
    public Response downloadManyCsv(@QueryParam("branchNo") Integer branchNo) {
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
                .setMaxResults(5)
                .getResultList();

        String csv = "customer_id,customer_name,branch_no\n" +
                customers.stream()
                        .map(customer -> customer.getCustomerId() + "," + customer.getCustomerName() + "," + customer.getBranchNo())
                        .collect(Collectors.joining("\n"));

        if (!customers.isEmpty()) {
            csv += "\n";
        }

        return Response.ok(csv)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"customers.csv\"")
                .type(MediaType.valueOf("text/csv; charset=UTF-8"))
                .build();
    }
}
