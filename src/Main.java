import TextDatabases.Exceptions.TextDatabaseException;
import TextDatabases.TextDatabase;

public class Main {
    public static void main(String[] args) {
        var sup = new ClienteSupplier (0, "");
        var db = new TextDatabase<> ("./data/clientes.tsv", sup);

        try {
            db.loadFromDisc ( );

            var cliente = new Cliente (0, "José");
            db.upsert (cliente);

            db.prune ( );

            db.saveToDisc ( );
        } catch (TextDatabaseException e) {
            System.out.println ("Erro: " + e + " causa: " + e.getCause ( ));
        }
    }
}

