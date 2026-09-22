package domain.port;

public interface FeedMetadataProvider {
    FeedMetadata fetch(String feedUrl);

    record FeedMetadata(String title, String description, String siteUrl) {
    }
}