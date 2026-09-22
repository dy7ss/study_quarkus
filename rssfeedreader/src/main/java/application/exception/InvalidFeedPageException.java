package application.exception;

public class InvalidFeedPageException extends RuntimeException {
    public InvalidFeedPageException(int page, int size) {
        super("page must be >= 0 and size must be between 1 and 100: page=" + page + ", size=" + size);
    }
}