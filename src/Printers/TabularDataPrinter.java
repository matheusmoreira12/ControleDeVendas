package Printers;

import TextDatabases.*;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

import static TextDatabases.StaticDefaults.ID_LIST_SEPARATOR;

public class TabularDataPrinter<TRecord extends DBRecord> {
    private final TextDBTable<TRecord> table;
    private final Map<String, String> substitutions;
    private final Map<Type, Function<Object, String>> customFormatters;

    public TabularDataPrinter(TextDBTable<TRecord> recordTextDBTable, Map<String, String> substitutions, Map<Type, Function<Object, String>> customFormatters) {
        this.table = recordTextDBTable;
        this.substitutions = substitutions;
        this.customFormatters = customFormatters;
    }

    public void printToStdout(int[] ids) {
        DBTableSchema schema = table.getSchema();

        List<List<String>> tabularData = new ArrayList<>();

        tabularData.add(schema.resolveColumns()
                .map(col -> {
                    String key = col.getKey();
                    return substitutions.getOrDefault(key, key);
                })
                .toList());

        boolean shouldFilterById = ids.length > 0;

        tabularData.addAll(table.select(record -> !shouldFilterById ||
                        Arrays.stream(ids).anyMatch(id -> id == record.getId()))
                .map(record -> schema.resolveColumns()
                        .map(col -> switch (col) {
                            case DBColumnDefinition def -> convertColumn(record, def);
                            case DBRelation rel -> convertRelation(record, rel);
                            case DBManyRelation mRel -> convertManyRelation(record, mRel);
                            default -> throw new IllegalStateException("Unexpected value: " + col);
                        })
                        .toList())
                .toList());

        int[] columnWidths = getColumnWidths(tabularData);

        System.out.println("╭" +
                String.join("┬", Arrays.stream(columnWidths).mapToObj("─"::repeat).toList()) +
                "╮");

        int rowCount = tabularData.size();

        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            List<String> row = tabularData.get(rowIndex);
            int colCount = row.size();

            System.out.print("│");

            for (int colIndex = 0; colIndex < colCount; colIndex++) {
                int colWidth = columnWidths[colIndex];

                String col = row.get(colIndex);

                if (col == null)
                    System.out.print(" ".repeat(colWidth));
                else if (rowIndex == 0)
                    System.out.print(StringUtils.padBoth(col, ' ', colWidth));
                else
                    System.out.print(StringUtils.padEnd(col, ' ', colWidth));

                System.out.print("│");
            }

            System.out.println();

            if (rowIndex == 0) {
                System.out.println("├" +
                        String.join("┼", Arrays.stream(columnWidths).mapToObj("─"::repeat).toList()) +
                        "┤");
            }
        }

        System.out.println("╰" +
                String.join("┴", Arrays.stream(columnWidths).mapToObj("─"::repeat).toList()) +
                "╯");

        System.out.println("[" + (tabularData.size() - 1) + " Resultado(s) Encontrado(s)]");
    }

    public void printToStdout() {
        printToStdout(new int[0]);
    }

    private String convertManyRelation(TRecord record, DBManyRelation mRel) {
        return String.join(ID_LIST_SEPARATOR + " ", mRel.getListAccessor().apply(record).stream()
                .filter(Objects::nonNull)
                .map(relRec -> Integer.toString(relRec.getId()))
                .toList());
    }

    private String convertRelation(TRecord record, DBRelation rel) {
        DBRecord relRecord = rel.getAccessor().apply(record);
        if (relRecord == null)
            return null;

        return Integer.toString(relRecord.getId());
    }

    private String convertColumn(TRecord record, DBColumnDefinition column) {
        String recordStr = customFormatters.getOrDefault(column.getValueType(),
                        column.getParserFormatter()::format)
                .apply(column.getAccessor().apply(record));

        return substitutions.getOrDefault(recordStr, recordStr);
    }

    private int[] getColumnWidths(List<List<String>> tabularData) {
        int[] widths = (int[]) Array.newInstance(int.class, tabularData.getFirst().size());

        for (var row : tabularData) {
            for (int i = 0; i < widths.length; i++) {
                if (row.size() <= i)
                    continue;

                String rowStr = row.get(i);
                if (rowStr == null)
                    continue;

                int width = rowStr.length();

                if (width > widths[i])
                    widths[i] = width;
            }
        }

        return widths;
    }
}
