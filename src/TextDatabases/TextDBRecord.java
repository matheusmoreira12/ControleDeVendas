package TextDatabases;

import java.io.Reader;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

public abstract class TextDBRecord {
    private OffsetDateTime createdDate;
    private int id;

    public TextDBRecord() {
        this.createdDate = OffsetDateTime.now();
    }

    public Stream<String> serialize() {
        return Arrays.stream (new String[]{
                Integer.toString (id),
                TextDBUtils.formatDate (createdDate),
        });
    }

    public void deserialize(Stream<String> columnStream) {
        Iterator<String> iterator = columnStream.iterator();

        id = Integer.parseInt (iterator.next());
        createdDate = TextDBUtils.parseDate (iterator.next());
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public int getId() {
        return id;
    }
}
