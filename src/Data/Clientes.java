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
    protected DBTableSchema getTableSchema() {
        List<DBTableSchemaColumnDefinition> columns = new ArrayList<>();

        columns.add(new DBTableSchemaColumnDefinition(new StringConverter(),
                "Nome",
                String.class,
                record -> ((Cliente) record).getNome(),
                (record, value) -> ((Cliente) record).setNome((String) value))
        );

        return new DBTableSchema(columns, super.getTableSchema());
    }

    @Override
    protected Supplier<Cliente> getRecordSupplier() {
        return new ClienteSupplier(0, "");
    }
}
