package TextDatabases.ValueConverters;

import TextDatabases.DBColumnParserFormatter;

public class IntegerParserFormatter extends DBColumnParserFormatter {
    @Override
    public Object parse(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public String format(Object value) {
        return Integer.toString((Integer) value);
    }
}
