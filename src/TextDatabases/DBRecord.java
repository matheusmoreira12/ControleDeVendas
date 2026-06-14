package TextDatabases;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a record in a text DB table.
 */
public abstract class DBRecord {
    private OffsetDateTime createdDate;
    private int id;

    public DBRecord(int id) {
        this.id = id;
        this.createdDate = OffsetDateTime.now();
    }

    public DBRecord(int id, OffsetDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
