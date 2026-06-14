package TextDatabases;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class DBTableSchemaColumnDefinition<TValue, TRecord extends DBRecord> {
    public DBTableSchemaColumnDefinition(DBColumnConverter<TValue> converter,
                                         String label,
                                         Function<TRecord, TValue> getter,
                                         BiConsumer<TRecord, TValue> setter) {
        this.converter = converter;
        this.label = label;
        this.getter = getter;
        this.setter = setter;
    }

    public DBColumnConverter<TValue> converter;
    public String label;
    private final Function<TRecord, TValue> getter;
    private final BiConsumer<TRecord, TValue> setter;

    public DBColumnConverter<TValue> getConverter() {
        return converter;
    }

    public String getLabel() {
        return label;
    }

    public Function<TRecord, TValue> getGetter() {
        return getter;
    }

    public BiConsumer<TRecord, TValue> getSetter() {
        return setter;
    }
}
