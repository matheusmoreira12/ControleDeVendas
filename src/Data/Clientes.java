package Data;

import TextDatabases.DBTableSchema;
import TextDatabases.DBTableSchemaColumnDefinition;
import TextDatabases.TextDBTable;
import TextDatabases.ValueConverters.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Clientes extends TextDBTable<Cliente> {
    public Clientes(String fileName) {
        super(fileName);
    }

    @Override
    protected DBTableSchema<Cliente> getTableSchema() {
        List<DBTableSchemaColumnDefinition<?, Cliente>> columns = new ArrayList<>();

        columns.add(new DBTableSchemaColumnDefinition<>(new StringConverter(),
                "Nome",
                Cliente::getNome,
                Cliente::setNome)
        );

        return new DBTableSchema<Cliente>(columns, super.getTableSchema());
    }

    @Override
    protected Supplier<Cliente> getRecordSupplier() {
        return new ClienteSupplier(0, "");
    }
}
