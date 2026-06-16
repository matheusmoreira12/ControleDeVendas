package Data;

import TextDatabases.DBRecord;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sale extends DBRecord {
    private LocalDate soldDate;
    private final List<SaleItem> items;
    private SaleKind kind;
    private Client client;

    public Sale(int id, LocalDate soldDate, Client client, SaleKind kind) {
        super(id);
        this.soldDate = soldDate;
        this.client = client;
        this.items = new ArrayList<>();
        this.kind = kind;
    }

    public LocalDate getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(LocalDate soldDate) {
        this.soldDate = soldDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public SaleKind getKind() {
        return kind;
    }

    public void setKind(SaleKind kind) {
        this.kind = kind;
    }
}

