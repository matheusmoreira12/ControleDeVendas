package TextDatabases.ValueConverters;

import TextDatabases.DBColumnConverter;

public class StringConverter extends DBColumnConverter<String> {
    @Override
    public String convert(String value) {
        return value;
    }

    @Override
    public String convertBack(String value) {
        return value;
    }
}
