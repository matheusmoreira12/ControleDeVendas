package Data;

import TextDatabases.DBRecord;

public class Cliente extends DBRecord {
    public Cliente(int id, String nome) {
        super(id);

        this.nome = nome;
    }

    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
