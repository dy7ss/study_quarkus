package domain.entity;

import java.net.URI;
import java.time.LocalDateTime;

import domain.exception.InvalidFeedException;

public class Feed {
    private Long id;
    private final String feedUrl;
    private final String title;
    private final String description;
    private final String siteUrl;
    private final LocalDateTime lastFetchedAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private Feed(
            Long id,
            String feedUrl,
            String title,
            String description,
            String siteUrl,
            LocalDateTime lastFetchedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.feedUrl = requireUrl(feedUrl);
        this.title = requireText(title, "title", 255);
        this.description = description;
        this.siteUrl = siteUrl;
        this.lastFetchedAt = lastFetchedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Feed register(String feedUrl, String title, String description, String siteUrl, LocalDateTime now) {
        return new Feed(null, feedUrl, title, description, siteUrl, null, now, now);
    }

    public static Feed restore(
            Long id,
            String feedUrl,
            String title,
            String description,
            String siteUrl,
            LocalDateTime lastFetchedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return new Feed(id, feedUrl, title, description, siteUrl, lastFetchedAt, createdAt, updatedAt);
    }

    public Long id() {
        return id;
    }

    public String feedUrl() {
        return feedUrl;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public String siteUrl() {
        return siteUrl;
    }

    public LocalDateTime lastFetchedAt() {
        return lastFetchedAt;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Feed ID is already assigned");
        }
        this.id = id;
    }

    private static String requireUrl(String value) {
        if (value == null || value.isBlank() || value.length() > 2048) {
            throw new InvalidFeedException("feedUrl must be a non-empty URL of at most 2048 characters");
        }
        URI uri;
        try {
            uri = URI.create(value);
        } catch (IllegalArgumentException exception) {
            throw new InvalidFeedException("feedUrl must be a valid URL");
        }
        if (!("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()))
                || uri.getHost() == null) {
            throw new InvalidFeedException("feedUrl must be an HTTP or HTTPS URL");
        }
        return value;
    }

    private static String requireText(String value, String field, int maxLength) {
        if (value == null || value.isBlank() || value.length() > maxLength) {
            throw new InvalidFeedException(field + " must be non-empty and at most " + maxLength + " characters");
        }
        return value;
    }
}