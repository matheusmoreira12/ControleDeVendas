package TextDatabases.ValueConverters;

import TextDatabases.DBColumnParserFormatter;

public class StringParserFormatter extends DBColumnParserFormatter {
    @Override
    public Object parse(String value) {
        return value;
    }

    @Override
    public String format(Object value) {
        return (String) value;
    }
}
