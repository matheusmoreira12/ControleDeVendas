package TextDatabases;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DBColumnDefinitionBase {
    private final String key;
    private final Type valueType;
    private final Function<DBRecord, Object> getter;
    private final BiConsumer<DBRecord, Object> setter;

    public DBColumnDefinitionBase(String key,
                              Type valueType,
                              Function<DBRecord, Object> getter,
                              BiConsumer<DBRecord, Object> setter) {
        this.key = key;
        this.valueType = valueType;
        this.getter = getter;
        this.setter = setter;
    }

    public String getKey() {
        return key;
    }

    public Type getValueType() {
        return valueType;
    }

    public Function<DBRecord, Object> getGetter() {
        return getter;
    }

    public BiConsumer<DBRecord, Object> getSetter() {
        return setter;
    }
}
