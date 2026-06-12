package TextDatabases;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

public abstract class TextDBRecord {
    private OffsetDateTime createdDate;
    private int id;

    public TextDBRecord() {
        this.createdDate = OffsetDateTime.now();
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

    public int getId() {
        return id;
    }
}
