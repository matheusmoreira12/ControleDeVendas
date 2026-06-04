package TextDatabases.Exceptions;

public class ETextDatabaseReadFailed extends TextDatabaseException {
    private static final String DEFAULT_MESSAGE = "A gravação do banco de dados em texto falhou.";

    public ETextDatabaseReadFailed(String message, Throwable cause) {
        super(message, cause);
    }

    public ETextDatabaseReadFailed(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public ETextDatabaseReadFailed() {
        super(DEFAULT_MESSAGE, null);
    }
}
