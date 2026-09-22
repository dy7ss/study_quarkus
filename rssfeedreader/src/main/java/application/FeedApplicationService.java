package application;

import java.time.Clock;
import java.time.LocalDateTime;

import application.dto.FeedPage;
import application.dto.RegisterFeedCommand;
import application.exception.FeedNotFoundException;
import application.exception.InvalidFeedPageException;
import domain.entity.Feed;
import domain.exception.DuplicateFeedException;
import domain.port.FeedMetadataProvider;
import domain.repository.FeedRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FeedApplicationService {

    @Inject
    FeedRepository repository;

    @Inject
    FeedMetadataProvider metadataProvider;

    private final Clock clock = Clock.systemUTC();

    @Transactional
    public Feed register(RegisterFeedCommand command) {
        if (repository.findByUrl(command.feedUrl()).isPresent()) {
            throw new DuplicateFeedException(command.feedUrl());
        }
        var metadata = metadataProvider.fetch(command.feedUrl());
        return repository
                .save(Feed.register(command.feedUrl(), metadata.title(), metadata.description(), metadata.siteUrl(),
                        LocalDateTime.now(clock)));
    }

    @Transactional
    public Feed get(long id) {
        return repository.findById(id).orElseThrow(() -> new FeedNotFoundException(id));
    }

    @Transactional
    public FeedPage list(int page, int size) {
        if (page < 0 || size < 1 || size > 100) {
            throw new InvalidFeedPageException(page, size);
        }
        return new FeedPage(repository.findAll(page, size), page, size, repository.count());
    }

    @Transactional
    public void delete(long id) {
        if (!repository.deleteById(id)) {
            throw new FeedNotFoundException(id);
        }
    }
}