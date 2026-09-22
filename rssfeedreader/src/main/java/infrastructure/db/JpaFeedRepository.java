package infrastructure.db;

import java.util.List;
import java.util.Optional;

import domain.entity.Feed;
import domain.repository.FeedRepository;
import infrastructure.db.entity.FeedEntity;
import infrastructure.db.mapper.FeedEntityMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class JpaFeedRepository implements FeedRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Feed save(Feed feed) {
        FeedEntity entity = FeedEntityMapper.toEntity(feed);
        entityManager.persist(entity);
        feed.assignId(entity.id);
        return feed;
    }

    @Override
    public Optional<Feed> findById(long id) {
        return Optional.ofNullable(entityManager.find(FeedEntity.class, id)).map(FeedEntityMapper::toDomain);
    }

    @Override
    public Optional<Feed> findByUrl(String feedUrl) {
        return entityManager.createQuery("select f from FeedEntity f where f.feedUrl = :feedUrl", FeedEntity.class)
                .setParameter("feedUrl", feedUrl).getResultStream().findFirst().map(FeedEntityMapper::toDomain);
    }

    @Override
    public List<Feed> findAll(int page, int size) {
        return entityManager.createQuery("select f from FeedEntity f order by f.id desc", FeedEntity.class)
                .setFirstResult(page * size).setMaxResults(size).getResultList().stream()
                .map(FeedEntityMapper::toDomain).toList();
    }

    @Override
    public long count() {
        return entityManager.createQuery("select count(f) from FeedEntity f", Long.class).getSingleResult();
    }

    @Override
    public boolean deleteById(long id) {
        FeedEntity entity = entityManager.find(FeedEntity.class, id);
        if (entity == null)
            return false;
        entityManager.remove(entity);
        return true;
    }

}