package Data;

import TextDatabases.DBRecord;

import java.math.BigDecimal;

public class Product extends DBRecord {
    private String code;
    private String description;
    private BigDecimal price;

    public Product(int id, String code, String description, BigDecimal price) {
        super(id);
        this.code = code;
        this.description = description;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
