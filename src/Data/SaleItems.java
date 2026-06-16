package Data;

import TextDatabases.*;
import TextDatabases.ValueConverters.DoubleParserFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SaleItems extends TextDBTable<SaleItem> {
    private final Sales salesTable;
    private final Products productsTable;

    public SaleItems(String fileName, Sales salesTable, Products productsTable) {
        super(fileName);
        this.salesTable = salesTable;
        this.productsTable = productsTable;
    }

    @Override
    public DBTableSchema getSchema() {
        List<DBColumnDefinitionBase> columns = new ArrayList<>();

        columns.add(new DBRelation("sale",
                salesTable,
                Sale.class,
                DBUtils.getRelationValue(SaleItem::getSale),
                DBUtils.setRelationValue(SaleItem::setSale)));

        columns.add(new DBRelation("product",
                productsTable,
                Product.class,
                DBUtils.getRelationValue(SaleItem::getProduct),
                DBUtils.setRelationValue(SaleItem::setProduct)));

        columns.add(new DBColumnDefinition("quantity",
                new DoubleParserFormatter(),
                double.class,
                DBUtils.getColumnValue(SaleItem::getQuantity),
                DBUtils.setColumnValue(SaleItem::setQuantity)));

        return new DBTableSchema(columns, super.getSchema());
    }

    @Override
    public Supplier<SaleItem> getRecordSupplier() {
        return () -> new SaleItem(getUniqueId(), null, null, 0.0);
    }
}
