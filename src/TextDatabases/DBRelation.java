package TextDatabases;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DBRelation extends DBColumnDefinitionBase {
    private final TextDBTable<?> table;

    public DBRelation(String key,
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
