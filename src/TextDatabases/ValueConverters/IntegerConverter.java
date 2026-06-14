package TextDatabases.ValueConverters;

import TextDatabases.DBColumnConverter;

public class IntegerConverter extends DBColumnConverter {
    @Override
    public Object convert(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public String convertBack(Object value) {
        return Integer.toString((Integer) value);
    }
}
