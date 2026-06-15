package TextDatabases;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DBColumnDefinition extends DBColumnDefinitionBase {
    private final DBColumnConverter converter;
    private final Function<DBRecord, Object> getter;
    private final BiConsumer<DBRecord, Object> setter;

    public DBColumnDefinition(String key, DBColumnConverter converter, Type valueType, Function<DBRecord, Object> getter, BiConsumer<DBRecord, Object> setter) {
        super(key, valueType);

        this.converter = converter;
        this.getter = getter;
        this.setter = setter;
    }

    public Function<DBRecord, Object> getGetter() {
        return getter;
    }

    public BiConsumer<DBRecord, Object> getSetter() {
        return setter;
    }

    public DBColumnConverter getConverter() {
        return converter;
    }
}
