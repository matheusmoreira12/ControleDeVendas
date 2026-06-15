package Data;

import TextDatabases.*;
import TextDatabases.ValueConverters.DecimalConverter;
import TextDatabases.ValueConverters.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Products extends TextDBTable<Product> {
    public Products(String fileName) {
        super(fileName);
    }

    @Override
    protected Supplier<Product> getRecordSupplier() {
        return () -> new Product(0, null, null, null);
    }

    @Override
    public DBTableSchema getSchema() {
        List<DBColumnDefinitionBase> columns = new ArrayList<>();

        columns.add(new DBColumnDefinition("code",
                new StringConverter(),
                String.class,
                TextDBUtils.getColumnValue(Product::getCode),
                TextDBUtils.setColumnValue(Product::setCode)));

        columns.add(new DBColumnDefinition("description",
                new StringConverter(),
                String.class,
                TextDBUtils.getColumnValue(Product::getDescription),
                TextDBUtils.setColumnValue(Product::setDescription)));

        columns.add(new DBColumnDefinition("price",
                new DecimalConverter(),
                String.class,
                TextDBUtils.getColumnValue(Product::getPrice),
                TextDBUtils.setColumnValue(Product::setPrice)));

        return new DBTableSchema(columns, super.getSchema());
    }
}
