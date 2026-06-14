import Data.Cliente;
import Data.ClienteSupplier;
import Data.Clientes;
import TextDatabases.Exceptions.TextDatabaseException;

public class Main {
    public static void main(String[] args) {
        var sup = new ClienteSupplier(0, "");
        var db = new Clientes("./data/clientes.tsv");

        try {
            db.load( );

            var cliente = new Cliente(0, "José");
            db.upsert (cliente);

            db.prune ( );

            db.save( );
        } catch (TextDatabaseException e) {
            System.out.println ("Erro: " + e + " causa: " + e.getCause ( ));
        }
    }
}

