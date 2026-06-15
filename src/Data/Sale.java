package Data;

import TextDatabases.DBRecord;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sale extends DBRecord {
    private OffsetDateTime soldDate;
    private final List<Product> items;
    private SaleKind kind;
    private Client client;

    public Sale(int id, OffsetDateTime soldDate, Client client, SaleKind kind) {
        super(id);
        this.soldDate = soldDate;
        this.client = client;
        this.items = new ArrayList<>();
        this.kind = kind;
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

    public List<Product> getItems() {
        return items;
    }

    public SaleKind getKind() {
        return kind;
    }

    public void setKind(SaleKind kind) {
        this.kind = kind;
    }
}

