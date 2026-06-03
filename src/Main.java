import TextDatabases.ITextDBRecord;
import TextDatabases.TextDatabase;
import TextDatabases.TextDatabaseWriteException;

import java.time.LocalDate;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        var sup = new ClienteSupplier (0, "");
        var db = new TextDatabase<> ("clientes.txt", sup);

        try {
            var cliente = new Cliente (0, "José");
            db.save (cliente);
            db.saveToDisc ( );
        } catch (TextDatabaseWriteException e) {
            System.out.println ("Erro: " + e + " causa: " + e.getCause ( ));
        }
    }
}

class ClienteSupplier implements Supplier<Cliente> {
    private final int defaultId;
    private final String defaultNome;

    public ClienteSupplier(int defaultId, String defaultNome) {
        this.defaultId = defaultId;
        this.defaultNome = defaultNome;
    }

    @Override
    public Cliente get() {
        return new Cliente (defaultId, defaultNome);
    }
}

class Cliente implements ITextDBRecord {
    public Cliente(int id, String nome) {
        this.id = id;
        this.nome = nome;

        createdDate = LocalDate.now ( );
    }

    private int id;
    private String nome;
    private LocalDate createdDate;

    @Override
    public String[] toText() {
        return new String[]{
                Integer.toString (id),
                createdDate.toString ( ),
                nome
        };
    }

    @Override
    public void readFromText(String[] columns) {
        id = Integer.parseInt (columns[0]);
        createdDate = LocalDate.parse (columns[1]);
        nome = columns[2];
    }

    @Override
    public LocalDate getCreatedDate() {
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
