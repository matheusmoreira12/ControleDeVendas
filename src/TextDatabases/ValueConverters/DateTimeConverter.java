package TextDatabases.ValueConverters;

import TextDatabases.DBColumnConverter;
import TextDatabases.StaticDefaults;

import java.time.OffsetDateTime;

public class DateTimeConverter extends DBColumnConverter<OffsetDateTime> {
    @Override
    public OffsetDateTime convert(String value) {
        return OffsetDateTime.from(StaticDefaults.DB_DATETIME_FORMATTER.parse(value));
    }

    @Override
    public String convertBack(OffsetDateTime value) {
        return StaticDefaults.DB_DATETIME_FORMATTER.format(value);
    }
}
