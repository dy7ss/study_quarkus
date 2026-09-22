package application.dto;

import java.util.List;

import domain.entity.Feed;

public record FeedPage(List<Feed> items, int page, int size, long total) {
}
