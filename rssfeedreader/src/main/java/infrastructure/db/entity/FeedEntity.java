package infrastructure.db.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "feeds", uniqueConstraints = @UniqueConstraint(name = "uk_feeds_feed_url", columnNames = "feed_url"))
public class FeedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    public Long id;

    @Column(name = "feed_url", nullable = false, length = 2048)
    public String feedUrl;
    @Column(nullable = false, length = 255)
    public String title;
    @Column(columnDefinition = "TEXT")
    public String description;
    @Column(name = "site_url", length = 2048)
    public String siteUrl;
    @Column(name = "last_fetched_at")
    public LocalDateTime lastFetchedAt;
    @Column(name = "created_at", nullable = false)
    public LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    public LocalDateTime updatedAt;
}