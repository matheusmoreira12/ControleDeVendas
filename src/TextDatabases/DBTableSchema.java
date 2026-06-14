package TextDatabases;

import java.util.LinkedList;
import java.util.List;

public class DBTableSchema {
    private final List<DBTableSchemaColumnDefinition> columns;
    private DBTableSchema extendsSchema;

    public DBTableSchema(List<DBTableSchemaColumnDefinition> columns) {
        this.columns = columns;
        this.extendsSchema = null;
    }

    public DBTableSchema(List<DBTableSchemaColumnDefinition> columns, DBTableSchema extendsSchema) {
        this.columns = columns;
        this.extendsSchema = extendsSchema;
    }

    public List<DBTableSchemaColumnDefinition> getColumns() {
        return columns;
    }

    public DBTableSchema getExtendsSchema() {
        return extendsSchema;
    }

    @SuppressWarnings("unchecked")
    public void serialize(DBRecord record, List<String> columnStrs) {
        if (extendsSchema != null)
            extendsSchema.serialize(record, columnStrs);

        for (var column : columns) {
            var value = column.getGetter().apply(record);
            var valueStr = column.getConverter().convertBack(value);
            columnStrs.add(valueStr);
        }
    }

    @SuppressWarnings("unchecked")
    public void deserialize(DBRecord record, List<String> columnStrs) {
        if (extendsSchema != null)
            extendsSchema.deserialize(record, columnStrs);

        for (var column : columns) {
            var valueStr = columnStrs.removeFirst();
            var value = column.getConverter().convert(valueStr);
            column.getSetter().accept(record, value);
        }
    }
}
