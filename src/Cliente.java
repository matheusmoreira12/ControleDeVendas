import TextDatabases.TextDBRecord;

import java.time.OffsetDateTime;
import java.util.*;

public class Cliente extends TextDBRecord {
    public Cliente(int id, String nome) {
        this.id = id;
        this.nome = nome;

        createdDate = OffsetDateTime.now ( );
    }

    private int id;
    private String nome;
    private OffsetDateTime createdDate;

    @Override
    public void serialize(ArrayList<String> columns) {
        super.serialize(columns);

        columns.add(nome);
    }

    @Override
    public void deserialize(LinkedList<String> columns) {
        super.deserialize (columns);

        this.nome = columns.removeLast();
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
