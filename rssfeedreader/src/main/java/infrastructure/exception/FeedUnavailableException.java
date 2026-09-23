package infrastructure.exception;

public class FeedUnavailableException extends RuntimeException {
    public FeedUnavailableException(String message) {
        super(message);
    }

    public FeedUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
