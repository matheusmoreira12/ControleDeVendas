package TextDatabases.ValueConverters;

import TextDatabases.DBColumnConverter;

public class BooleanConverter extends DBColumnConverter<Boolean> {
    @Override
    public Boolean convert(String value) {
        return Boolean.parseBoolean(value);
    }

    @Override
    public String convertBack(Boolean value) {
        return Boolean.toString(value);
    }
}
