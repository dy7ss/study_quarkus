package domain.repository;

import java.util.List;
import java.util.Optional;

import domain.entity.Feed;

public interface FeedRepository {
    Feed save(Feed feed);

    Optional<Feed> findById(long id);

    Optional<Feed> findByUrl(String feedUrl);

    List<Feed> findAll(int page, int size);

    long count();

    void deleteById(long id);
}