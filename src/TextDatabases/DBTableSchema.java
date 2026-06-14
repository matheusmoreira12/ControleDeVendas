package TextDatabases;

import java.util.List;
import java.util.stream.Stream;

public class DBTableSchema<TRecord extends DBRecord> {
    private final List<DBTableSchemaColumnDefinition<?, TRecord>> columns;
    private DBTableSchema<TRecord> extendsSchema;

    public DBTableSchema(List<DBTableSchemaColumnDefinition<?, TRecord>> columns) {
        this.columns = columns;
    }

    public DBTableSchema(List<DBTableSchemaColumnDefinition<?, TRecord>> columns, DBTableSchema<TRecord> extendsSchema) {
        this.columns = columns;
        this.extendsSchema = extendsSchema;
    }

    public List<DBTableSchemaColumnDefinition<?, TRecord>> getColumns() {
        return columns;
    }

    public DBTableSchema getExtendsSchema() {
        return extendsSchema;
    }

    public Stream<DBTableSchemaColumnDefinition<?, TRecord>> resolveColumns() {
        return Stream.concat(extendsSchema.resolveColumns(), columns.stream());
    }
}
