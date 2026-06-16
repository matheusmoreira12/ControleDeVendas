package TextDatabases.ValueConverters;

import TextDatabases.DBColumnParserFormatter;

public class BooleanParserFormatter extends DBColumnParserFormatter {
    @Override
    public Object parse(String value) {
        return Boolean.parseBoolean(value);
    }

    @Override
    public String format(Object value) {
        return Boolean.toString((Boolean) value);
    }
}
