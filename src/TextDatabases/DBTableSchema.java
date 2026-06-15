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
        var rec = (DBRecord) relation.getGetter().apply(record);
        return Integer.toString(rec.getId());
    }

    public String serializeManyRelation(DBRecord record, DBManyRelation relation) {
        var list = relation.getListAccessor().apply(record);

        List<String> valuesStr = list.stream()
                .map(DBRecord::getId)
                .map(i -> Integer.toString(i))
                .toList();

        return String.join(ID_LIST_SEPARATOR, valuesStr);
    }

    public void deserialize(DBRecord record, List<String> columnStrs) {
        if (extendsSchema != null)
            extendsSchema.deserialize(record, columnStrs);

        for (var column : columns) {
            var valueStr = columnStrs.removeFirst();

            switch (column) {
                case DBColumnDefinition def -> deserializeColumn(record, valueStr, def);
                case DBRelation rel -> deserializeRelation(record, valueStr, rel);
                case DBManyRelation mRel -> deserializeManyRelation(record, valueStr, mRel);
                case null, default -> throw new RuntimeException();
            }
        }
    }

    private void deserializeColumn(DBRecord record, String valueStr, DBColumnDefinition column) {
        var value = column.getConverter().convert(valueStr);
        column.getSetter().accept(record, value);
    }

    private void deserializeRelation(DBRecord record, String valueStr, DBRelation relation) {
        int id = Integer.parseInt(valueStr);
        var relRecord = relation.getTable()
                .select(r -> r.getId() == id)
                .findFirst()
                .orElseThrow();

        relation.getSetter().accept(record, relRecord);
    }

    @SuppressWarnings("unchecked")
    private void deserializeManyRelation(DBRecord record, String valueStr, DBManyRelation manyRelation) {
        Stream<Integer> ids = Arrays.stream(valueStr.split(ID_LIST_SEPARATOR))
                .map(Integer::parseInt);

        var relRecords = ids.map(id -> manyRelation
                        .getTable()
                        .select(r -> r.getId() == id)
                        .findFirst()
                        .orElseThrow())
                .toList();

        var list = manyRelation.getListAccessor().apply(record);
        list.clear();
        list.addAll(relRecords);
    }

}
