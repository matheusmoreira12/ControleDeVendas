import Data.*;
import Printers.TabularDataPrinter;
import TextDatabases.*;
import TextDatabases.Exceptions.TextDatabaseException;
import TextDatabases.ValueConverters.EnumUtils;

import java.io.Console;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;

import static TextDatabases.StaticDefaults.ID_LIST_SEPARATOR;

public class Main {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");

    private static Clients clients;
    private static Products products;
    private static Sales sales;
    private static SaleItems saleItems;
    private static Map<String, String> substitutions;
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

            substitutions = Map.ofEntries(
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
                    substitutions,
                    customFormatters);

            var productsPrinter = new TabularDataPrinter<>(
                    products,
                    substitutions,
                    customFormatters
            );

            var salesPrinter = new TabularDataPrinter<>(
                    sales,
                    substitutions,
                    customFormatters
            );

            var saleItemsPrinter = new TabularDataPrinter<>(
                    saleItems,
                    substitutions,
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

    private static String formatDateTime(Object dateTime) {
        return DATE_TIME_FORMATTER.format((OffsetDateTime) dateTime);
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
            switch (column) {
                case DBColumnDefinition def -> fillRecordColumn(stdin, table, record, def);
                case DBRelation rel -> fillRecordRelation(stdin, table, record, rel);
                case DBManyRelation mRel -> fillRecordManyRelation(stdin, table, record, mRel);
                default -> throw new IllegalStateException("Unexpected value: " + column);
            }
        }

        return record;
    }

    private static <TRecord extends DBRecord> void fillRecordManyRelation(Scanner stdin, TextDBTable<TRecord> table, TRecord record, DBManyRelation mRel) {
    }

    private static <TRecord extends DBRecord> void fillRecordRelation(Scanner stdin, TextDBTable<TRecord> table, TRecord record, DBRelation relation) {
        String key = relation.getKey();
        String substKey = substitutions.getOrDefault(key, key);

        boolean success = false;

        while (!success) {
            System.out.print("Id de " + substKey + ": ");

            try {
                int id = stdin.nextInt();
                stdin.nextLine();

                var relRecord = relation.getTable()
                        .select(DBUtils.selectMatchingId(id))
                        .findFirst()
                        .orElseThrow();

                relation.getModifier().accept(record, relRecord);

                success = true;
            } catch (NoSuchElementException e) {
                System.out.println("\tId de " + substKey + " incorreto.");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <TRecord extends DBRecord> void fillRecordColumn(Scanner stdin,
                                                                    TextDBTable<TRecord> table,
                                                                    TRecord record,
                                                                    DBColumnDefinition definition) {
        String key = definition.getKey();
        String substKey = substitutions.getOrDefault(key, key);

        boolean success = false;

        while (!success) {
            try {
                Type valueType = definition.getValueType();

                System.out.print(substKey + ": ");

                if (EnumUtils.typeIsEnum(valueType)) {
                    System.out.println();
                    printAllEnumOptions((Class<Enum<?>>) valueType);
                }

                var value = ingestColumnValue(stdin, definition);

                definition.getModifier().accept(record, value);

                success = true;
            } catch (DateTimeParseException e) {
                System.out.println("\tData com formato inválido.");
            } catch (IllegalArgumentException e) {
                System.out.println("\tValor fora do intervalo esperado.");
            }
        }
    }

    private static <TEnum extends Enum<?>> void printAllEnumOptions(Class<TEnum> enumClass) {
        if (enumClass.isEnum()) {
            System.out.println("\tOpções:");

            var constants = enumClass.getEnumConstants();

            for (var constant : constants) {
                String constName = constant.name();
                String substConstName = substitutions.getOrDefault(constName, constName);

                System.out.println("\t\t" + constant.ordinal() + " = " + substConstName);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Object ingestColumnValue(Scanner stdin, DBColumnDefinition definition) {
        Type valueType = definition.getValueType();

        if (valueType == String.class)
            return stdin.next() + stdin.nextLine();
        else {
            Object value;

            if (EnumUtils.typeIsEnum(valueType)) {
                int ordinal = stdin.nextInt();
                var constantsByOrdinal = EnumUtils.constantsByOrdinal((Class<Enum<?>>) valueType);
                return constantsByOrdinal.get(ordinal);
            }
            if (valueType == int.class)
                value = stdin.nextInt();
            else if (valueType == double.class)
                value = stdin.nextInt();
            else {
                var valueStr = stdin.next();
                stdin.nextLine();

                var converter = customParsers.get(definition.getValueType());
                if (converter == null)
                    return definition.getParserFormatter().parse(valueStr);

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
            System.out.println("4. Consultar Itens de uma Venda");
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