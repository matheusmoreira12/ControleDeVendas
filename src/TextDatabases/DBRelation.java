package TextDatabases;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DBRelation extends DBColumnDefinitionBase {
    private final Function<DBRecord, DBRecord> accessor;
    private final BiConsumer<DBRecord, DBRecord> modifier;
    private final TextDBTable<?> table;

    public DBRelation(String key,
                      TextDBTable<?> table,
                      Type valueType,
                      Function<DBRecord, DBRecord> accessor,
                      BiConsumer<DBRecord, DBRecord> modifier) {
        super(key, valueType);

        this.accessor = accessor;
        this.modifier = modifier;
        this.table = table;
    }

    public Function<DBRecord, DBRecord> getAccessor() {
        return accessor;
    }

    public BiConsumer<DBRecord, DBRecord> getModifier() {
        return modifier;
    }

    public TextDBTable<?> getTable() {
        return table;
    }
}
