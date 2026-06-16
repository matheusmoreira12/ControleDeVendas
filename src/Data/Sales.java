package Data;

import TextDatabases.*;
import TextDatabases.ValueConverters.DateParserFormatter;
import TextDatabases.ValueConverters.EnumParserFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Sales extends TextDBTable<Sale> {
    private final Clients clientsTable;
    private final Products productsTable;

    public Sales(String fileName, Clients clientsTable, Products productsTable) {
        super(fileName);

        this.clientsTable = clientsTable;
        this.productsTable = productsTable;
    }

    @Override
    public Supplier<Sale> getRecordSupplier() {
        return () -> new Sale(getUniqueId(), null, null, null);
    }

    @Override
    public DBTableSchema getSchema() {
        List<DBColumnDefinitionBase> columns = new ArrayList<>();

        columns.add(new DBColumnDefinition(
                "soldDate",
                new DateParserFormatter(),
                LocalDate.class,
                DBUtils.getColumnValue(Sale::getSoldDate),
                DBUtils.setColumnValue(Sale::setSoldDate)));

        columns.add(new DBRelation(
                "client",
                clientsTable,
                Client.class,
                DBUtils.getRelationValue(Sale::getClient),
                DBUtils.setRelationValue(Sale::setClient)));

        columns.add(new DBManyRelation(
                "items",
                productsTable,
                SaleItem.class,
                DBUtils.getManyRelationList(Sale::getItems)
        ));

        columns.add(new DBColumnDefinition(
                "kind",
                new EnumParserFormatter<>(SaleKind.class),
                SaleKind.class,
                DBUtils.getColumnValue(Sale::getKind),
                DBUtils.setColumnValue(Sale::setKind)));

        return new DBTableSchema(columns, super.getSchema());
    }
}
