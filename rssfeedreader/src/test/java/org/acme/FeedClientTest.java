package org.acme;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeedImpl;

class FeedClientTest {

  @Test
  void mapsCamelRssFeed() {
    SyndContentImpl description = new SyndContentImpl();
    description.setValue("概要です");
    SyndEntryImpl entry = new SyndEntryImpl();
    entry.setTitle("記事タイトル");
    entry.setLink("https://zenn.dev/example/articles/1");
    entry.setPublishedDate(new Date(1704067200000L));
    entry.setAuthor("author");
    entry.setDescription(description);
    SyndFeedImpl feed = new SyndFeedImpl();
    feed.setTitle("Zenn Feed");
    feed.setEntries(List.of(entry));

    FeedClient.FeedResponse response = FeedClient.toResponse(feed);

    assertEquals("Zenn Feed", response.title());
    assertEquals(1, response.articles().size());
    FeedClient.FeedArticle article = response.articles().get(0);
    assertEquals("記事タイトル", article.title());
    assertEquals("https://zenn.dev/example/articles/1", article.link());
    assertEquals("2024-01-01T00:00:00Z", article.publishedAt());
    assertEquals("author", article.author());
    assertEquals("概要です", article.description());
  }
}
