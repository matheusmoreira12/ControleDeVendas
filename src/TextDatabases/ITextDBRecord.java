package TextDatabases;

import java.time.LocalDate;

public interface ITextDBRecord {
    String[] toText();

    void readFromText(String[] columns);

    LocalDate getCreatedDate();

    int getId();
}
