package TextDatabases;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DBRelation extends DBColumnDefinitionBase {
    private final Function<DBRecord, DBRecord> getter;
    private final BiConsumer<DBRecord, DBRecord> setter;
    private final TextDBTable<?> table;

    public DBRelation(String key,
                      TextDBTable<?> table,
                      Type valueType,
                      Function<DBRecord, DBRecord> getter,
                      BiConsumer<DBRecord, DBRecord> setter) {
        super(key, valueType);

        this.getter = getter;
        this.setter = setter;
        this.table = table;
    }

    public Function<DBRecord, DBRecord> getGetter() {
        return getter;
    }

    public BiConsumer<DBRecord, DBRecord> getSetter() {
        return setter;
    }

    public TextDBTable<?> getTable() {
        return table;
    }
}
