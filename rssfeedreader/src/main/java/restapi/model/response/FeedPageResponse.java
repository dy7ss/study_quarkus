package restapi.model.response;

import java.util.List;

public record FeedPageResponse(List<FeedResponse> items, int page, int size, long total) {
}