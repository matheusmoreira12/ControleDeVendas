package TextDatabases.ValueConverters;

import TextDatabases.DBColumnParserFormatter;
import TextDatabases.StaticDefaults;

import java.time.OffsetDateTime;

public class DateTimeParserFormatter extends DBColumnParserFormatter {
    @Override
    public Object parse(String value) {
        return OffsetDateTime.from(StaticDefaults.DB_DATETIME_FORMATTER.parse(value));
    }

    @Override
    public String format(Object value) {
        return StaticDefaults.DB_DATETIME_FORMATTER.format((OffsetDateTime)value);
    }
}
