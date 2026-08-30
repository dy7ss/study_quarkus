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
}
