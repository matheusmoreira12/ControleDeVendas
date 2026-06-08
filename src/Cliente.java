import TextDatabases.ITextDBRecord;
import TextDatabases.TextDBUtils;

import java.time.OffsetDateTime;

public class Cliente implements ITextDBRecord {
    public Cliente(int id, String nome) {
        this.id = id;
        this.nome = nome;

        createdDate = OffsetDateTime.now ( );
    }

    private int id;
    private String nome;
    private OffsetDateTime createdDate;

    @Override
    public String[] toText() {
        return new String[]{
                Integer.toString (id),
                TextDBUtils.formatDate (createdDate),
                nome
        };
    }

    @Override
    public void readFromText(String[] columns) {
        id = Integer.parseInt (columns[0]);
        createdDate = TextDBUtils.parseDate (columns[1]);
        nome = columns[2];
    }

    @Override
    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
