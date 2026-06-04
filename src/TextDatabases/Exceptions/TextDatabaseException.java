package TextDatabases.Exceptions;

public abstract class TextDatabaseException extends RuntimeException {
    public TextDatabaseException(String message) {
        super(message);
    }

    public TextDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TextDatabaseException(Throwable cause) {
        super(cause);
    }
}
