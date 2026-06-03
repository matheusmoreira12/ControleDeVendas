package TextDatabases;

public class TextDatabaseReadException extends RuntimeException {
    public TextDatabaseReadException(String message, Throwable cause) {
        super (message, cause);
    }
}
