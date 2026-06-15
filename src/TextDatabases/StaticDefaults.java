package TextDatabases;

import java.time.format.DateTimeFormatter;

public class StaticDefaults {
    static public final String COLUMN_SEPARATOR = Character.toString(31);
    static public final String ID_LIST_SEPARATOR = ",";
    static public final DateTimeFormatter FILE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
    static public final DateTimeFormatter DB_DATETIME_FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;
}
