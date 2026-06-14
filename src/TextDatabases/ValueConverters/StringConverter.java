package TextDatabases.ValueConverters;

import TextDatabases.DBColumnConverter;

public class StringConverter extends DBColumnConverter {
    @Override
    public Object convert(String value) {
        return value;
    }

    @Override
    public String convertBack(Object value) {
        return (String) value;
    }
}
