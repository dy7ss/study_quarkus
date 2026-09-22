package restapi.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpServer;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class FeedResourceTest {

    private static HttpServer rssServer;
    private static String rssServerUrl;

    @BeforeAll
    static void startRssServer() throws IOException {
        rssServer = HttpServer.create(new InetSocketAddress(0), 0);
        rssServer.createContext("/", exchange -> {
            byte[] response = """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <rss version="2.0">
                      <channel>
                        <title>Test Feed</title>
                        <description>Test feed description</description>
                        <link>https://example.test/</link>
                      </channel>
                    </rss>
                    """.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/rss+xml");
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream output = exchange.getResponseBody()) {
                output.write(response);
            }
        });
        rssServer.start();
        rssServerUrl = "http://localhost:" + rssServer.getAddress().getPort();
    }

    @AfterAll
    static void stopRssServer() {
        rssServer.stop(0);
    }

    @Test
    void registerReturnsCreatedFeed() {
        String feedUrl = newFeedUrl();

        given()
                .contentType("application/json")
                .body("{\"feedUrl\":\"" + feedUrl + "\"}")
                .when()
                .post("/feeds")
                .then()
                .statusCode(201)
                .header("Location", containsString("/feeds/"))
                .body("feedId", notNullValue())
                .body("feedUrl", org.hamcrest.Matchers.equalTo(feedUrl))
                .body("title", org.hamcrest.Matchers.equalTo("Test Feed"));
    }

    @Test
    void listReturnsRegisteredFeeds() {
        String feedUrl = registerFeed();

        given()
                .queryParam("size", 100)
                .when()
                .get("/feeds")
                .then()
                .statusCode(200)
                .body("items.feedUrl", hasItem(feedUrl))
                .body("page", org.hamcrest.Matchers.equalTo(0))
                .body("size", org.hamcrest.Matchers.equalTo(100));
    }

    @Test
    void getReturnsRegisteredFeed() {
        String feedUrl = newFeedUrl();
        long feedId = registerFeedAndGetId(feedUrl);

        given()
                .when()
                .get("/feeds/{feedId}", feedId)
                .then()
                .statusCode(200)
                .body("feedId", org.hamcrest.Matchers.equalTo((int) feedId))
                .body("feedUrl", org.hamcrest.Matchers.equalTo(feedUrl))
                .body("title", org.hamcrest.Matchers.equalTo("Test Feed"));
    }

    @Test
    void deleteRemovesRegisteredFeed() {
        long feedId = registerFeedAndGetId(newFeedUrl());

        given()
                .when()
                .delete("/feeds/{feedId}", feedId)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/feeds/{feedId}", feedId)
                .then()
                .statusCode(404);
    }

    private static String registerFeed() {
        String feedUrl = newFeedUrl();
        registerFeedAndGetId(feedUrl);
        return feedUrl;
    }

    private static long registerFeedAndGetId(String feedUrl) {
        return given()
                .contentType("application/json")
                .body("{\"feedUrl\":\"" + feedUrl + "\"}")
                .when()
                .post("/feeds")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("feedId");
    }

    private static String newFeedUrl() {
        return URI.create(rssServerUrl + "/feed-" + UUID.randomUUID() + ".xml").toString();
    }
}