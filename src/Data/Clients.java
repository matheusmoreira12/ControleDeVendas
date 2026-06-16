package Data;

import TextDatabases.*;
import TextDatabases.ValueConverters.StringParserFormatter;

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
                new StringParserFormatter(),
                String.class,
                DBUtils.getColumnValue(Client::getName),
                DBUtils.setColumnValue(Client::setName)));

        columns.add(new DBColumnDefinition(
                "address",
                new StringParserFormatter(),
                String.class,
                DBUtils.getColumnValue(Client::getAddress),
                DBUtils.setColumnValue(Client::setAddress)));

        columns.add(new DBColumnDefinition(
                "phone",
                new StringParserFormatter(),
                String.class,
                DBUtils.getColumnValue(Client::getPhone),
                DBUtils.setColumnValue(Client::setPhone)));

        return new DBTableSchema(columns, super.getSchema());
    }

    @Override
    public Supplier<Client> getRecordSupplier() {
        return () -> new Client(getUniqueId(), null, null, null);
    }
}
