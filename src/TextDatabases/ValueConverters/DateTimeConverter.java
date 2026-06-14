package TextDatabases.ValueConverters;

import TextDatabases.DBColumnConverter;
import TextDatabases.StaticDefaults;

import java.time.OffsetDateTime;

public class DateTimeConverter extends DBColumnConverter {
    @Override
    public Object convert(String value) {
        return OffsetDateTime.from(StaticDefaults.DB_DATETIME_FORMATTER.parse(value));
    }

    @Override
    public String convertBack(Object value) {
        return StaticDefaults.DB_DATETIME_FORMATTER.format((OffsetDateTime)value);
    }
}
