package mytimeacty.exception;

public class UnauthoriedException extends RuntimeException {
    public UnauthoriedException(String message) {
        super(message);
    }
}