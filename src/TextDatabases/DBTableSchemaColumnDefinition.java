package TextDatabases;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DBTableSchemaColumnDefinition {
    public DBTableSchemaColumnDefinition(DBColumnConverter converter,
                                         String label,
                                         Type valueType,
                                         Function<DBRecord, Object> getter,
                                         BiConsumer<DBRecord, Object> setter) {
        this.converter = converter;
        this.label = label;
        this.valueType = valueType;
        this.getter = getter;
        this.setter = setter;
    }

    public DBColumnConverter converter;
    public String label;
    private final Type valueType;
    private final Function<DBRecord, Object> getter;
    private final BiConsumer<DBRecord, Object> setter;

    public DBColumnConverter getConverter() {
        return converter;
    }

    public String getLabel() {
        return label;
    }

    public Function getGetter() {
        return getter;
    }

    public BiConsumer getSetter() {
        return setter;
    }

    public Type getValueType() {
        return valueType;
    }
}
