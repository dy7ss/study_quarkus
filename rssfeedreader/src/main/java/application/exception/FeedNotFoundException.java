package application.exception;

public class FeedNotFoundException extends RuntimeException {
    public FeedNotFoundException(long id) {
        super("Feed was not found: " + id);
    }
}