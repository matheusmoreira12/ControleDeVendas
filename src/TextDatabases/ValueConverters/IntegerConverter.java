package TextDatabases.ValueConverters;

import TextDatabases.DBColumnConverter;

public class IntegerConverter extends DBColumnConverter<Integer> {
    @Override
    public Integer convert(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public String convertBack(Integer value) {
        return Integer.toString(value);
    }
}
