package Data;

import TextDatabases.*;
import TextDatabases.ValueConverters.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Clients extends TextDBTable<Client> {
    public Clients(String fileName) {
        super(fileName);
    }

    @Override
    public DBTableSchema getSchema() {
        List<DBColumnDefinitionBase> columns = new ArrayList<>();

        columns.add(new DBColumnDefinition(
                "name",
                new StringConverter(),
                String.class,
                TextDBUtils.getColumnValue(Client::getName),
                TextDBUtils.setColumnValue(Client::setName)));

        columns.add(new DBColumnDefinition(
                "address",
                new StringConverter(),
                String.class,
                TextDBUtils.getColumnValue(Client::getAddress),
                TextDBUtils.setColumnValue(Client::setAddress)));

        columns.add(new DBColumnDefinition(
                "phone",
                new StringConverter(),
                String.class,
                TextDBUtils.getColumnValue(Client::getPhone),
                TextDBUtils.setColumnValue(Client::setPhone)));

        return new DBTableSchema(columns, super.getSchema());
    }

    @Override
    protected Supplier<Client> getRecordSupplier() {
        return () -> new Client(0, null, null, null);
    }
}
