package TextDatabases;

import java.lang.reflect.Type;

public class DBColumnDefinitionBase {
    private final String key;
    private final Type valueType;

    public DBColumnDefinitionBase(String key,
                              Type valueType) {
        this.key = key;
        this.valueType = valueType;
    }

    public String getKey() {
        return key;
    }

    public Type getValueType() {
        return valueType;
    }
}
