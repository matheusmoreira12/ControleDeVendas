package Data;

import TextDatabases.DBRecord;

import java.time.OffsetDateTime;

public class Sale extends DBRecord {
    private OffsetDateTime soldDate;
    private Product[] items;
    private SaleKind type;
    private Client client;

    public Sale(int id, OffsetDateTime soldDate, Client client, Product[] items, SaleKind kind) {
        super(id);
        this.soldDate = soldDate;
        this.client = client;
        this.items = items;
        this.type = kind;
    }

    public OffsetDateTime getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(OffsetDateTime soldDate) {
        this.soldDate = soldDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Product[] getItems() {
        return items;
    }

    public void setItems(Product[] items) {
        this.items = items;
    }

    public SaleKind getType() {
        return type;
    }

    public void setType(SaleKind type) {
        this.type = type;
    }
}

