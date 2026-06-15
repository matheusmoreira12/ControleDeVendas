package TextDatabases.ValueConverters;

import TextDatabases.DBColumnConverter;

import java.math.BigDecimal;

public class DecimalConverter extends DBColumnConverter {
    @Override
    public Object convert(String value) {
        return new BigDecimal(value);
    }

    @Override
    public String convertBack(Object value) {
        return value.toString();
    }
}
