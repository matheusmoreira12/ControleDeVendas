package TextDatabases.Exceptions;

public class TextDatabaseBackupFailedException extends TextDatabaseException {
    private static final String DEFAULT_MESSAGE = "O backup dos dados falhou.";

    public TextDatabaseBackupFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TextDatabaseBackupFailedException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public TextDatabaseBackupFailedException() {
        super(DEFAULT_MESSAGE, null);
    }
}
