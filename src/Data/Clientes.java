package Data;

import TextDatabases.*;
import TextDatabases.ValueConverters.IntegerConverter;
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

        columns.add(new DBTableSchemaColumnDefinition(new IntegerConverter(),
                "Código",
                Integer.class,
                TextDBUtils.getColumnValue(Cliente::getCodigo),
                TextDBUtils.setColumnValue(Cliente::setCodigo))
        );

        columns.add(new DBTableSchemaColumnDefinition(new StringConverter(),
                "Nome",
                String.class,
                TextDBUtils.getColumnValue(Cliente::getNome),
                TextDBUtils.setColumnValue(Cliente::setNome))
        );

        columns.add(new DBTableSchemaColumnDefinition(new StringConverter(),
                "Endereço",
                String.class,
                TextDBUtils.getColumnValue(Cliente::getEndereco),
                TextDBUtils.setColumnValue(Cliente::setEndereco))
        );

        columns.add(new DBTableSchemaColumnDefinition(new StringConverter(),
                "Telefone",
                String.class,
                TextDBUtils.getColumnValue(Cliente::getTelefone),
                TextDBUtils.setColumnValue(Cliente::setTelefone))
        );

        return new DBTableSchema(columns, super.getTableSchema());
    }

    @Override
    protected Supplier<Cliente> getRecordSupplier() {
        return new ClienteSupplier();
    }
}
