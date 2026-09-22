package restapi.model.response;

import java.time.LocalDateTime;

import domain.entity.Feed;

public record FeedResponse(Long feedId, String feedUrl, String title, String description, String siteUrl,
        LocalDateTime lastFetchedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static FeedResponse from(Feed feed) {
        return new FeedResponse(feed.id(), feed.feedUrl(), feed.title(), feed.description(), feed.siteUrl(),
                feed.lastFetchedAt(), feed.createdAt(), feed.updatedAt());
    }
}