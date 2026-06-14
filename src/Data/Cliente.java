package Data;

import TextDatabases.DBRecord;

public class Cliente extends DBRecord {
    public Cliente(int id, int codigo, String nome, String endereco, String telefone) {
        super(id);
        this.codigo = codigo;

        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
    }

    private int codigo;
    private String nome;
    private String endereco;
    private String telefone;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
