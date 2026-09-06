package org.acme;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;

@QuarkusTest
class CsvDownloadResourceTest {

    @Test
    void shouldDownloadCsv() {
        given()
                .when().get("/csv")
                .then()
                .statusCode(200)
                .contentType("text/csv")
                .header("Content-Disposition", containsString("attachment; filename=\"sample.csv\""))
                .body(is("id,name,age\n1,Alice,30\n2,Bob,25\n"));
    }

    @Test
    void shouldDownloadManyCsvFromDatabaseWithoutStreaming() {
        given()
                .when().get("/csv/many")
                .then()
                .statusCode(200)
                .contentType("text/csv")
                .header("Content-Disposition", containsString("attachment; filename=\"customers.csv\""))
                .body(is("customer_id,customer_name,branch_no\n1,customer_001,1\n2,customer_002,2\n3,customer_003,1\n4,customer_004,3\n5,customer_005,2\n"));
    }

    @Test
    void shouldDownloadManyCsvFromDatabaseWithStreaming() {
        given()
                .queryParam("streaming", "true")
                .when().get("/csv/many")
                .then()
                .statusCode(200)
                .contentType("text/csv")
                .header("Content-Disposition", containsString("attachment; filename=\"customers.csv\""))
                .body(is("customer_id,customer_name,branch_no\n1,customer_001,1\n2,customer_002,2\n3,customer_003,1\n4,customer_004,3\n5,customer_005,2\n"));
    }

    @Test
    void shouldDownloadManyCsvFilteredByBranchNoWithoutStreaming() {
        given()
                .queryParam("branchNo", 2)
                .when().get("/csv/many")
                .then()
                .statusCode(200)
                .contentType("text/csv")
                .header("Content-Disposition", containsString("attachment; filename=\"customers.csv\""))
                .body(is("customer_id,customer_name,branch_no\n2,customer_002,2\n5,customer_005,2\n"));
    }
}
