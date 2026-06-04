package TextDatabases;

import java.time.LocalDateTime;

public interface ITextDBRecord {
    String[] toText();

    void readFromText(String[] columns);

    LocalDateTime getCreatedDate();

    int getId();
}
