package Data;

import TextDatabases.*;
import TextDatabases.ValueConverters.DecimalParserFormatter;
import TextDatabases.ValueConverters.StringParserFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Products extends TextDBTable<Product> {
    public Products(String fileName) {
        super(fileName);
    }

    @Override
    public Supplier<Product> getRecordSupplier() {
        return () -> new Product(getUniqueId(), null, null, null);
    }

    @Override
    public DBTableSchema getSchema() {
        List<DBColumnDefinitionBase> columns = new ArrayList<>();

        columns.add(new DBColumnDefinition("code",
                new StringParserFormatter(),
                String.class,
                DBUtils.getColumnValue(Product::getCode),
                DBUtils.setColumnValue(Product::setCode)));

        columns.add(new DBColumnDefinition("description",
                new StringParserFormatter(),
                String.class,
                DBUtils.getColumnValue(Product::getDescription),
                DBUtils.setColumnValue(Product::setDescription)));

        columns.add(new DBColumnDefinition("price",
                new DecimalParserFormatter(),
                String.class,
                DBUtils.getColumnValue(Product::getPrice),
                DBUtils.setColumnValue(Product::setPrice)));

        return new DBTableSchema(columns, super.getSchema());
    }
}
