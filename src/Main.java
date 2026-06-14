import Data.Cliente;
import Data.Clientes;
import TextDatabases.Exceptions.TextDatabaseException;

public class Main {
    static void main(String[] ignoredArgs) {
        var db = new Clientes("./data/clientes.tsv");

        try {
            db.load();

            var cliente = new Cliente(0, 0, "José", "R. Antonim do Fofofó, 420", "5555");
            db.upsert(cliente);

            db.prune();

            db.save();
        } catch (TextDatabaseException e) {
            System.out.println("Erro: " + e + " causa: " + e.getCause());
        }
    }
}