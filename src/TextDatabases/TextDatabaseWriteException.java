package TextDatabases;

public class TextDatabaseWriteException extends Throwable {
    private static final String DEFAULT_MESSAGE = "A leitura do banco de dados em texto falhou.";

    public TextDatabaseWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public TextDatabaseWriteException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public TextDatabaseWriteException() {
        super(DEFAULT_MESSAGE, null);
    }
}
