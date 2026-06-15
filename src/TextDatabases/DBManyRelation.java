package TextDatabases;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DBManyRelation extends DBColumnDefinitionBase {
    private final TextDBTable<?> table;

    public DBManyRelation(String key,
                          TextDBTable<?> table,
                          Type valueType,
                          Function<DBRecord, Object> getter,
                          BiConsumer<DBRecord, Object> setter) {
        super(key, valueType, getter, setter);

        this.table = table;
    }

    public TextDBTable<?> getTable() {
        return table;
    }
}
