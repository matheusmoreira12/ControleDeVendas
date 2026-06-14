package TextDatabases;

import java.util.List;

public record DBTableSchema(List<DBTableSchemaColumnDefinition> columns, DBTableSchema extendsSchema) {
    public DBTableSchema(List<DBTableSchemaColumnDefinition> columns) {
        this(columns, null);
    }

    public void serialize(DBRecord record, List<String> columnStrs) {
        if (extendsSchema != null)
            extendsSchema.serialize(record, columnStrs);

        for (var column : columns) {
            var value = column.getGetter().apply(record);
            var valueStr = column.getConverter().convertBack(value);
            columnStrs.add(valueStr);
        }
    }

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
