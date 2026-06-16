package TextDatabases.ValueConverters;

import TextDatabases.DBColumnParserFormatter;
import TextDatabases.StaticDefaults;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class DateParserFormatter extends DBColumnParserFormatter {
    @Override
    public Object parse(String value) {
        return LocalDate.from(StaticDefaults.DB_DATE_FORMATTER.parse(value));
    }

    @Override
    public String format(Object value) {
        return StaticDefaults.DB_DATE_FORMATTER.format((LocalDate)value);
    }
}
