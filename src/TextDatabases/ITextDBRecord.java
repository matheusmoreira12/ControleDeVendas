package TextDatabases;

import java.time.OffsetDateTime;

public interface ITextDBRecord {
    String[] toText();

    void readFromText(String[] columns);

    OffsetDateTime getCreatedDate();

    int getId();
}
