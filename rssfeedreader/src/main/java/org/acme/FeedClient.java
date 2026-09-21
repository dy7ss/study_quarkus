package org.acme;

import java.time.Duration;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FeedClient {

    private final CamelContext camelContext;
    private final String feedUrl;
    private final Duration requestTimeout;

    @Inject
    public FeedClient(
            CamelContext camelContext,
            @ConfigProperty(name = "rss.feed.url") String feedUrl,
            @ConfigProperty(name = "rss.feed.request-timeout") Duration requestTimeout) {
        this.camelContext = camelContext;
        this.feedUrl = feedUrl;
        this.requestTimeout = requestTimeout;
    }

    public FeedResponse read() {
        PollingConsumer consumer = null;
        try {
            Endpoint endpoint = camelContext.getEndpoint("rss:" + feedUrl + "?splitEntries=false");
            consumer = endpoint.createPollingConsumer();
            consumer.start();
            Exchange exchange = consumer.receive(requestTimeout.toMillis());
            if (exchange == null) {
                throw new FeedUnavailableException("RSS endpoint did not return a feed in time");
            }
            SyndFeed feed = exchange.getMessage().getBody(SyndFeed.class);
            if (feed == null) {
                throw new FeedParseException("RSS endpoint returned an empty feed");
            }
            return toResponse(feed);
        } catch (FeedParseException | FeedUnavailableException e) {
            throw e;
        } catch (Exception e) {
            throw new FeedUnavailableException("Could not fetch RSS feed", e);
        } finally {
            if (consumer != null) {
                try {
                    consumer.stop();
                } catch (Exception ignored) {
                }
            }
        }
    }

    static FeedResponse toResponse(SyndFeed feed) {
        List<FeedArticle> articles = feed.getEntries().stream()
                .map(FeedClient::toArticle)
                .toList();
        return new FeedResponse(feed.getTitle(), articles);
    }

    private static FeedArticle toArticle(SyndEntry entry) {
        String description = entry.getDescription() == null ? "" : entry.getDescription().getValue();
        String publishedAt = entry.getPublishedDate() == null ? "" : entry.getPublishedDate().toInstant().toString();
        return new FeedArticle(
                entry.getTitle(),
                entry.getLink(),
                publishedAt,
                entry.getAuthor(),
                description);
    }

    public record FeedResponse(String title, List<FeedArticle> articles) {
    }

    public record FeedArticle(String title, String link, String publishedAt, String author, String description) {
    }

    static class FeedUnavailableException extends RuntimeException {
        FeedUnavailableException(String message) {
            super(message);
        }

        FeedUnavailableException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static class FeedParseException extends RuntimeException {
        FeedParseException(String message) {
            super(message);
        }

        FeedParseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
