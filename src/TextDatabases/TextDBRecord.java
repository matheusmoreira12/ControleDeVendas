package TextDatabases;

import java.io.Reader;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

public abstract class TextDBRecord {
    private OffsetDateTime createdDate;
    private int id;

    public Stream<String> serialize() {
        return Arrays.stream (new String[]{
                Integer.toString (id),
                TextDBUtils.formatDate (createdDate),
        });
    }

    public void deserialize(Stream<String> columns) {
        id = Integer.parseInt (columns.findFirst ().orElseThrow ());
        createdDate = TextDBUtils.parseDate (columns.findFirst ().orElseThrow ());
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public int getId() {
        return id;
    }
}
