package TextDatabases;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static TextDatabases.StaticDefaults.ID_LIST_SEPARATOR;

@SuppressWarnings("ClassCanBeRecord")
public final class DBTableSchema {
    private final List<DBColumnDefinitionBase> columns;
    private final DBTableSchema extendsSchema;

    public DBTableSchema(List<DBColumnDefinitionBase> columns, DBTableSchema extendsSchema) {
        this.columns = columns;
        this.extendsSchema = extendsSchema;
    }

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

    private String serializeColumn(DBRecord record, DBColumnDefinition column) {
        var value = column.getAccessor().apply(record);
        return column.getParserFormatter().format(value);
    }

    private String serializeRelation(DBRecord record, DBRelation relation) {
        var relRecord = relation.getAccessor().apply(record);
        return Integer.toString(relRecord.getId());
    }

    private String serializeManyRelation(DBRecord record, DBManyRelation relation) {
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
        var value = column.getParserFormatter().parse(valueStr);
        column.getModifier().accept(record, value);
    }

    private void deserializeRelation(DBRecord record, String valueStr, DBRelation relation) {
        int id = Integer.parseInt(valueStr);

        var relRecord = relation.getTable()
                .select(DBUtils.selectMatchingId(id))
                .findFirst()
                .orElseThrow();

        relation.getModifier().accept(record, relRecord);
    }

    private void deserializeManyRelation(DBRecord record, String valueStr, DBManyRelation relation) {
        Stream<Integer> ids = Arrays.stream(valueStr.split(ID_LIST_SEPARATOR))
                .map(Integer::parseInt);

        var table = relation.getTable();
        var relRecords = ids.map(id -> table
                        .select(DBUtils.selectMatchingId(id))
                        .findFirst()
                        .orElseThrow())
                .toList();

        var list = relation.getListAccessor().apply(record);
        list.clear();
        list.addAll(relRecords);
    }


    public Stream<DBColumnDefinitionBase> resolveColumns() {
        if (extendsSchema != null)
            return Stream.concat(extendsSchema.resolveColumns(), columns.stream());

        return columns.stream();
    }

    public List<DBColumnDefinitionBase> getColumns() {
        return columns;
    }

    public DBTableSchema extendsSchema() {
        return extendsSchema;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DBTableSchema) obj;
        return Objects.equals(this.columns, that.columns) &&
                Objects.equals(this.extendsSchema, that.extendsSchema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, extendsSchema);
    }

    @Override
    public String toString() {
        return "DBTableSchema[" +
                "columns=" + columns + ", " +
                "extendsSchema=" + extendsSchema + ']';
    }

}
