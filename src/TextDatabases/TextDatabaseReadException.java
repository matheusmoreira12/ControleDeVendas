package TextDatabases;

public class TextDatabaseReadException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "A gravação do banco de dados em texto falhou.";

    public TextDatabaseReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public TextDatabaseReadException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public TextDatabaseReadException() {
        super(DEFAULT_MESSAGE, null);
    }
}
