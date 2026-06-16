package TextDatabases.ValueConverters;

import TextDatabases.DBColumnParserFormatter;

public class DoubleParserFormatter extends DBColumnParserFormatter {
    @Override
    public Object parse(String value) {
        return Double.parseDouble(value);
    }

    @Override
    public String format(Object value) {
        return Double.toString((double)value);
    }
}
