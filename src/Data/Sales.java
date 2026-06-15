package Data;

import TextDatabases.*;
import TextDatabases.ValueConverters.DateTimeConverter;

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
    protected Supplier<Sale> getRecordSupplier() {
        return () -> new Sale(0, null, null, null);
    }

    @Override
    public DBTableSchema getSchema() {
        List<DBColumnDefinitionBase> columns = new ArrayList<>();

        columns.add(new DBColumnDefinition(
                "soldDate",
                new DateTimeConverter(),
                String.class,
                TextDBUtils.getColumnValue(Sale::getSoldDate),
                TextDBUtils.setColumnValue(Sale::setSoldDate)));

        columns.add(new DBRelation(
                "client",
                clientsTable,
                Client.class,
                TextDBUtils.getRelationValue(Sale::getClient),
                TextDBUtils.setRelationValue(Sale::setClient)));

        columns.add(new DBManyRelation(
                "items",
                productsTable,
                Product.class,
                TextDBUtils.getManyRelationList(Sale::getItems)));

        return new DBTableSchema(columns, super.getSchema());
    }
}
