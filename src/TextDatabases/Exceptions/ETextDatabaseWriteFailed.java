package TextDatabases.Exceptions;

public class ETextDatabaseWriteFailed extends TextDatabaseException {
    private static final String DEFAULT_MESSAGE = "A gravação do banco de dados em texto falhou.";

    public ETextDatabaseWriteFailed(String message, Throwable cause) {
        super(message, cause);
    }

    public ETextDatabaseWriteFailed(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public ETextDatabaseWriteFailed() {
        super(DEFAULT_MESSAGE, null);
    }
}
