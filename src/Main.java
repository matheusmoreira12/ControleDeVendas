import Data.*;
import TextDatabases.Exceptions.TextDatabaseException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;

public class Main {
    static void main(String[] ignoredArgs) {
        try {
            var clients = new Clients("./data/clientes.mattdb");
            clients.load();

            var products = new Products("./data/produtos.mattdb");
            products.load();

            var sales = new Sales("./data/vendas.mattdb", clients, products);
            sales.load();

            var client = new Client(0, "José", "R. Antonim do Fofofó, 420", "5555");
            clients.upsert(client);

            var product = new Product(0, "056104610", "Borracha Escolar", BigDecimal.valueOf(15.00));
            products.upsert(product);

            var sale = new Sale(0, OffsetDateTime.now(), client, SaleKind.Cash);
            sales.upsert(sale);

            sale.getItems().add(product);
            sale.getItems().add(product);
            sale.getItems().add(product);
            sale.getItems().add(product);
            sale.getItems().add(product);

            clients.save();

            products.save();

            sales.save();
        } catch (TextDatabaseException e) {
            System.out.println("Erro: " + e + " causa: " + e.getCause());
        }
    }
}