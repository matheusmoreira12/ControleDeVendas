package TextDatabases.ValueConverters;

import TextDatabases.DBColumnConverter;

public class BooleanConverter extends DBColumnConverter {
    @Override
    public Object convert(String value) {
        return Boolean.parseBoolean(value);
    }

    @Override
    public String convertBack(Object value) {
        return Boolean.toString((Boolean) value);
    }
}
