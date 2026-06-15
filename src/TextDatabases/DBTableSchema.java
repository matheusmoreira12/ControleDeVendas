package TextDatabases;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static TextDatabases.StaticDefaults.ID_LIST_SEPARATOR;

public record DBTableSchema(List<DBColumnDefinitionBase> columns, DBTableSchema extendsSchema) {
    public DBTableSchema(List<DBColumnDefinitionBase> columns) {
        this(columns, null);
    }

    public void serialize(DBRecord record, List<String> columnStrs) {
        if (extendsSchema != null)
            extendsSchema.serialize(record, columnStrs);

        for (var column : columns) {
            String valueStr;

            switch (column) {
                case DBColumnDefinition def -> valueStr = serializeColumn(record, def);
                case DBRelation rel -> valueStr = serializeRelation(record, rel);
                case DBManyRelation mRel -> valueStr = serializeManyRelation(record, mRel);
                case null, default -> throw new RuntimeException();
            }

            columnStrs.add(valueStr);
        }
    }

    public String serializeColumn(DBRecord record, DBColumnDefinition column) {
        var value = column.getGetter().apply(record);
        return column.getConverter().convertBack(value);
    }

    public String serializeRelation(DBRecord record, DBRelation relation) {
        var obj = relation.getGetter().apply(record);

        if (obj instanceof DBRecord rec)
            return Integer.toString(rec.getId());
        else
            throw new RuntimeException();
    }

    public String serializeManyRelation(DBRecord record, DBManyRelation relation) {
        var obj = relation.getGetter().apply(record);

        if (obj instanceof DBRecord[] recs) {
            List<String> valuesStr = Arrays.stream(recs)
                    .map(DBRecord::getId)
                    .map(i -> Integer.toString(i))
                    .toList();

            return String.join(ID_LIST_SEPARATOR, valuesStr);
        } else
            throw new RuntimeException();
    }

    public void deserialize(DBRecord record, List<String> columnStrs) {
        if (extendsSchema != null)
            extendsSchema.deserialize(record, columnStrs);

        for (var column : columns) {
            var valueStr = columnStrs.removeFirst();
            Object value;

            switch (column) {
                case DBColumnDefinition def -> value = deserializeColumn(valueStr, def);
                case DBRelation rel -> value = deserializeRelation(valueStr, rel);
                case DBManyRelation mRel -> value = deserializeManyRelation(valueStr, mRel);
                case null, default -> throw new RuntimeException();
            }

            column.getSetter().accept(record, value);
        }
    }

    private Object deserializeColumn(String valueStr, DBColumnDefinition column) {
        return column.getConverter().convert(valueStr);
    }

    private Object deserializeRelation(String valueStr, DBRelation relation) {
        int id = Integer.parseInt(valueStr);
        return relation.getTable()
                .select(r -> r.getId() == id)
                .findFirst()
                .orElseThrow();
    }

    private Object deserializeManyRelation(String valueStr, DBManyRelation manyRelation) {
        Stream<Integer> ids = Arrays.stream(valueStr.split(ID_LIST_SEPARATOR))
                .map(Integer::parseInt);

        return ids.map(id -> manyRelation
                        .getTable()
                        .select(r -> r.getId() == id)
                        .findFirst()
                        .orElseThrow())
                .toArray();
    }

}
