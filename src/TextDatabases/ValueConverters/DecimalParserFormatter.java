package TextDatabases.ValueConverters;

import TextDatabases.DBColumnParserFormatter;

import java.math.BigDecimal;

public class DecimalParserFormatter extends DBColumnParserFormatter {
    @Override
    public Object parse(String value) {
        return new BigDecimal(value);
    }

    @Override
    public String format(Object value) {
        return value.toString();
    }
}
