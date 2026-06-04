package TextDatabases.Exceptions;

public class ETextDatabaseBackupFailed extends TextDatabaseException {
    private static final String DEFAULT_MESSAGE = "O backup dos dados falhou.";

    public ETextDatabaseBackupFailed(String message, Throwable cause) {
        super(message, cause);
    }

    public ETextDatabaseBackupFailed(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public ETextDatabaseBackupFailed() {
        super(DEFAULT_MESSAGE, null);
    }
}
