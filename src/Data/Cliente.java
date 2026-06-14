package Data;

import TextDatabases.DBRecord;

import java.util.*;

public class Cliente extends DBRecord {
    public Cliente(int id, String nome) {
        super(id);

        this.nome = nome;
    }

    private String nome;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
