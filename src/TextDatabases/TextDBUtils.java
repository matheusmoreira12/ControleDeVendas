package TextDatabases;

import java.time.OffsetDateTime;

import static TextDatabases.StaticDefaults.DB_DATETIME_FORMATTER;

public class TextDBUtils {
    public static String formatDate(OffsetDateTime date) {
        return DB_DATETIME_FORMATTER.format (date);
    }

    public static OffsetDateTime parseDate(String str) {
        return OffsetDateTime.from (DB_DATETIME_FORMATTER.parse (str));
    }
}
