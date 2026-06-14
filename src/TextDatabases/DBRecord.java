package TextDatabases;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

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

    public void serialize(ArrayList<String> columns) {
        columns.add(Integer.toString(id));
        columns.add(TextDBUtils.formatDate(createdDate));
    }

    public void deserialize(LinkedList<String> columns) {
        id = Integer.parseInt(columns.removeFirst());
        createdDate = TextDBUtils.parseDate(columns.removeFirst());
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
