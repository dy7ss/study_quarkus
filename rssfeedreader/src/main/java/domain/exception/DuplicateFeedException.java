package domain.exception;

public class DuplicateFeedException extends RuntimeException {
    public DuplicateFeedException(String feedUrl) {
        super("Feed is already registered: " + feedUrl);
    }
}