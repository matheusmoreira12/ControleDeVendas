package Data;

import TextDatabases.DBRecord;

import java.math.BigDecimal;

public class SaleItem extends DBRecord {
    private Sale sale;
    private Product product;
    private double quantity;

    public SaleItem(int id, Sale sale, Product product, double quantity) {
        super(id);
        this.sale = sale;
        this.product = product;
        this.quantity = quantity;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal computeTotal() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
