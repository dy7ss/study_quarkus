package infrastructure;

import java.time.Duration;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.rometools.rome.feed.synd.SyndFeed;

import domain.port.FeedMetadataProvider;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CamelFeedMetadataProvider implements FeedMetadataProvider {
    private final CamelContext camelContext;
    private final Duration requestTimeout;

    public CamelFeedMetadataProvider(
            CamelContext camelContext,
            @ConfigProperty(name = "rss.feed.request-timeout", defaultValue = "10S") Duration requestTimeout) {
        this.camelContext = camelContext;
        this.requestTimeout = requestTimeout;
    }

    @Override
    public FeedMetadata fetch(String feedUrl) {
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
            if (feed == null || feed.getTitle() == null || feed.getTitle().isBlank()) {
                throw new FeedParseException("RSS endpoint returned an invalid feed");
            }
            return new FeedMetadata(feed.getTitle(),
                    feed.getDescription(), feed.getLink());
        } catch (FeedUnavailableException | FeedParseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new FeedUnavailableException("Could not fetch RSS feed", exception);
        } finally {
            if (consumer != null) {
                try {
                    consumer.stop();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public static class FeedUnavailableException extends RuntimeException {
        public FeedUnavailableException(String message) {
            super(message);
        }

        public FeedUnavailableException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class FeedParseException extends RuntimeException {
        public FeedParseException(String message) {
            super(message);
        }
    }
}