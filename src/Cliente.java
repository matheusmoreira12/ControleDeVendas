import TextDatabases.TextDBRecord;
import TextDatabases.TextDBUtils;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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
    public Stream<String> serialize() {
        String[] appendedData = {
                nome
        };

        return Stream.concat (super.serialize ( ), Arrays.stream (appendedData));
    }

    @Override
    public void deserialize(Stream<String> columnsStream) {
        super.deserialize (columnsStream);

        this.nome = columnsStream.findFirst ( ).orElseThrow ( );
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
