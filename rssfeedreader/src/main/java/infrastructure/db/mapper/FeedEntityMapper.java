package infrastructure.db.mapper;

import domain.entity.Feed;
import infrastructure.db.entity.FeedEntity;

public final class FeedEntityMapper {
    private FeedEntityMapper() {
    }

    public static FeedEntity toEntity(Feed feed) {
        FeedEntity entity = new FeedEntity();
        entity.feedUrl = feed.feedUrl();
        entity.title = feed.title();
        entity.description = feed.description();
        entity.siteUrl = feed.siteUrl();
        entity.lastFetchedAt = feed.lastFetchedAt();
        entity.createdAt = feed.createdAt();
        entity.updatedAt = feed.updatedAt();
        return entity;
    }

    public static Feed toDomain(FeedEntity entity) {
        return Feed.restore(entity.id, entity.feedUrl, entity.title, entity.description, entity.siteUrl,
                entity.lastFetchedAt, entity.createdAt, entity.updatedAt);
    }
}