import Data.*;
import Printers.TabularDataPrinter;
import TextDatabases.*;
import TextDatabases.Exceptions.TextDatabaseException;

import javax.swing.text.DateFormatter;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;

import static TextDatabases.StaticDefaults.ID_LIST_SEPARATOR;

public class Main {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");

    private static Clients clients;
    private static Products products;
    private static Sales sales;
    private static SaleItems saleItems;
    private static Map<String, String> substitutionsMap;
    private static Map<TextDBTable<?>, TabularDataPrinter<?>> printers;
    private static Map<Type, Function<String, Object>> customParsers;
    private static Map<Type, Function<Object, String>> customFormatters;

    public static void main(String[] ignoredArgs) {
        try {
            clients = new Clients("./data/clientes.mattdb");
            clients.load();

            products = new Products("./data/produtos.mattdb");
            products.load();

            sales = new Sales("./data/vendas.mattdb", clients, products);
            sales.load();

            saleItems = new SaleItems("./data/itens_venda.mattdb", sales, products);
            saleItems.load();

            customFormatters = Map.of(OffsetDateTime.class, Main::formatDateTime,
                    LocalDate.class, Main::formatDate);

            customParsers = Map.of(OffsetDateTime.class, Main::parseDateTime,
                    LocalDate.class, Main::parseDate);

            substitutionsMap = Map.ofEntries(
                    Map.entry("client", "Cliente"),
                    Map.entry("name", "Nome"),
                    Map.entry("address", "Endereço"),
                    Map.entry("phone", "Telefone"),
                    Map.entry("soldDate", "Data da Venda"),
                    Map.entry("items", "Itens"),
                    Map.entry("kind", "Tipo"),
                    Map.entry("Cash", "À vista"),
                    Map.entry("Credit", "A prazo"),
                    Map.entry("sale", "Venda"),
                    Map.entry("product", "Produto"),
                    Map.entry("quantity", "Quantidade"));

            var clientsPrinter = new TabularDataPrinter<>(
                    clients,
                    substitutionsMap,
                    customFormatters);

            var productsPrinter = new TabularDataPrinter<>(
                    products,
                    substitutionsMap,
                    customFormatters
            );

            var salesPrinter = new TabularDataPrinter<>(
                    sales,
                    substitutionsMap,
                    customFormatters
            );

            var saleItemsPrinter = new TabularDataPrinter<>(
                    saleItems,
                    substitutionsMap,
                    customFormatters);

            printers = Map.of(clients, clientsPrinter,
                    products, productsPrinter,
                    sales, salesPrinter,
                    saleItems, saleItemsPrinter);

            Scanner stdin = new Scanner(System.in);
            showMainMenu(stdin);

            clients.prune();
            clients.save();

            products.prune();
            products.save();

            sales.prune();
            sales.save();

            saleItems.prune();
            saleItems.save();
        } catch (TextDatabaseException e) {
            System.out.println("Erro: " + e + " causa: " + e.getCause());
        }
    }

    private static String formatDateTime(Object date) {
        return DATE_TIME_FORMATTER.format((OffsetDateTime) date);
    }

    private static Object parseDateTime(String str) {
        if (Objects.equals(str, "agora"))
            return OffsetDateTime.now();

        return OffsetDateTime.from(DATE_TIME_FORMATTER.parse(str));
    }

    private static String formatDate(Object date) {
        return DATE_FORMATTER.format((LocalDate) date);
    }

    private static Object parseDate(String str) {
        if (Objects.equals(str, "hoje"))
            return LocalDate.now();

        return LocalDate.from(DATE_FORMATTER.parse(str));
    }

    private static void showMainMenu(Scanner stdin) {
        boolean exit = false;

        while (!exit) {
            System.out.println("0. Fechar");
            System.out.println("1. Menu Clientes");
            System.out.println("2. Menu Produtos");
            System.out.println("3. Menu Vendas");
            System.out.print("Opção: ");

            int command = stdin.nextInt();

            switch (command) {
                case 0:
                    exit = true;
                    break;
                case 1:
                    showMenu("Cliente", stdin, clients);
                    break;
                case 2:
                    showMenu("Produto", stdin, products);
                    break;
                case 3:
                    showSalesMenu(stdin);
                    break;
            }
        }
    }

    private static <TRecord extends DBRecord> void showMenu(String name, Scanner stdin, TextDBTable<TRecord> table) {
        boolean exit = false;

        while (!exit) {
            System.out.println("0. Sair");
            System.out.println("1. Cadastrar " + name);
            System.out.println("2. Consultar/Comparar " + name + "s");
            System.out.println("3. Exibir todos(as) " + name + "s");
            System.out.println("4. Excluir " + name);
            System.out.println("5. Alterar " + name);
            System.out.print("Opção: ");

            int command = stdin.nextInt();

            switch (command) {
                case 0:
                    exit = true;
                    break;
                case 1:
                    var record = createRecordAndFill(stdin, table);
                    table.insert(record);

                    break;
                case 2:
                    lookupRecord(stdin, table);
                    break;
                case 3:
                    showAll(table);
                    break;
                case 4:
                    deleteRecord(table);
                    break;
                case 5:
                    editRecord(table);
                    break;
            }
        }
    }

    private static <TRecord extends DBRecord> void editRecord(TextDBTable<TRecord> table) {
    }

    private static <TRecord extends DBRecord> void showAll(TextDBTable<TRecord> table) {
        printers.get(table).printToStdout();
    }

    private static <TRecord extends DBRecord> void lookupRecord(Scanner stdin, TextDBTable<TRecord> table) {
        System.out.print("Índice(s): ");

        var indexesStr = stdin.next();
        int[] indexes = Arrays.stream(indexesStr.split(ID_LIST_SEPARATOR))
                .mapToInt(Integer::parseInt)
                .toArray();

        printers.get(table).printToStdout(indexes);
    }

    private static <TRecord extends DBRecord> void deleteRecord(TextDBTable<TRecord> table) {
    }

    private static <TRecord extends DBRecord> TRecord createRecordAndFill(Scanner stdin, TextDBTable<TRecord> table) {
        var record = table.getRecordSupplier().get();

        var schema = table.getSchema();
        for (var column : schema.getColumns()) {
            if (column instanceof DBColumnDefinition def)
                fillRecordColumn(stdin, table, record, def);
        }

        return record;
    }

    private static <TRecord extends DBRecord> void fillRecordColumn(Scanner stdin,
                                                                    TextDBTable<TRecord> table,
                                                                    TRecord record,
                                                                    DBColumnDefinition definition) {
        String key = definition.getKey();
        String substKey = substitutionsMap.getOrDefault(key, key);

        System.out.println("Digite o(a) " + substKey + ": ");

        var value = ingestColumnValue(stdin, definition);

        definition.getModifier().accept(record, value);
    }

    private static Object ingestColumnValue(Scanner stdin, DBColumnDefinition definition) {
        Type valueType = definition.getValueType();

        if (valueType == String.class)
            return stdin.next() + stdin.nextLine();
        else {
            Object value;

            if (valueType == int.class)
                value = stdin.nextInt();
            else if (valueType == double.class)
                value = stdin.nextInt();
            else {
                var valueStr = stdin.next();
                var converter = customParsers.get(definition.getValueType());
                if (converter == null)
                    return definition.getParserFormatter().parse(valueStr);
                else
                    return converter.apply(valueStr);
            }

            stdin.nextLine();

            return value;
        }
    }

    private static void showSalesMenu(Scanner stdin) {
        boolean exit = false;

        while (!exit) {
            System.out.println("0. Sair");
            System.out.println("1. Fazer uma Venda");
            System.out.println("2. Exibir todos(as) Vendas");
            System.out.println("3. Consultar/Comparar Vendas");
            System.out.print("Opção: ");

            int command = stdin.nextInt();

            switch (command) {
                case 0:
                    exit = true;
                    break;
                case 1:
                    makeSale(stdin);
                    break;
                case 2:
                    showAll(sales);
                    break;
                case 3:
                    lookupRecord(stdin, sales);
                    break;
            }
        }
    }

    private static void makeSale(Scanner stdin) {
        Sale sale = createRecordAndFill(stdin, sales);
        sales.insert(sale);
    }
}