package TextDatabases;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

public class DBManyRelation extends DBColumnDefinitionBase {
    private final TextDBTable<?> table;
    private final Function<DBRecord, List<DBRecord>> listAccessor;

    public DBManyRelation(String key,
                          TextDBTable<?> table,
                          Type valueType,
                          Function<DBRecord, List<DBRecord>> listAccessor) {
        super(key, valueType);

        this.table = table;
        this.listAccessor = listAccessor;
    }

    public TextDBTable<?> getTable() {
        return table;
    }

    public Function<DBRecord, List<DBRecord>> getListAccessor() {
        return listAccessor;
    }
}
