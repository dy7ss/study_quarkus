package domain.exception;

public class InvalidFeedException extends RuntimeException {
    public InvalidFeedException(String message) {
        super(message);
    }
}