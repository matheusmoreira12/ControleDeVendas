package TextDatabases;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DBColumnDefinition extends DBColumnDefinitionBase {
    public final DBColumnConverter converter;

    public DBColumnDefinition(String key, DBColumnConverter converter, Type valueType, Function<DBRecord, Object> getter, BiConsumer<DBRecord, Object> setter) {
        super(key, valueType, getter, setter);

        this.converter = converter;
    }

    public DBColumnConverter getConverter() {
        return converter;
    }
}
