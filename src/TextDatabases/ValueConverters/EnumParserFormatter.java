package TextDatabases.ValueConverters;

import TextDatabases.DBColumnParserFormatter;

import java.util.Map;

public class EnumParserFormatter<TEnum extends Enum<TEnum>> extends DBColumnParserFormatter {
    private final Map<String, TEnum> constantsByName;
    private final Map<TEnum, String> namesByConstant;

    public EnumParserFormatter(Class<TEnum> enumClass) {
        this.constantsByName = EnumUtils.constantsByName(enumClass);
        this.namesByConstant = EnumUtils.namesByConstant(enumClass);
    }

    @Override
    public Object parse(String value) {
        return constantsByName.get(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String format(Object value) {
        return namesByConstant.get((TEnum) value);
    }
}
