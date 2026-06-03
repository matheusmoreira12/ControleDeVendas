package TextDatabases;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/// Allows the manipulation of TSV text databases
public class TextDatabase<TRecord extends ITextDBRecord> {
    static private final String BASE_PATH = ".\\data\\";

    static private final String COLUMN_SEPARATOR = "\t";

    private final String fileName;

    private final ArrayList<TRecord> records;

    private final Supplier<TRecord> recordSupplier;

    public TextDatabase(String fileName, Supplier<TRecord> recordSupplier) {
        this.fileName = BASE_PATH + fileName;
        this.recordSupplier = recordSupplier;
        this.records = new ArrayList<> ( );
    }

    /// Loads all records from a file, replacing old data with new data
    public void loadFromDisc() throws TextDatabaseReadException {
        File file = new File (fileName);

        try (BufferedReader reader = new BufferedReader (new FileReader (file))) {
            String oldFileNewName = fileName + "_" + LocalDate.now ( );
            File oldFile = new File (oldFileNewName);

            if (!file.exists ( )) {
                file.createNewFile ( );

                return; // Nothing to read. Return!
            }

            file.renameTo (oldFile);

            importRecordsFromTextFile (reader);
        } catch (IOException e) {
            throw new TextDatabaseReadException ("A leitura do banco de dados em texto falhou.", e);
        }
    }

    /// Imports all records from a file
    private void importRecordsFromTextFile(BufferedReader reader) throws IOException {
        String line;

        do {
            // Read line
            line = reader.readLine ( );

            // Parse record data
            String[] columnStrs = line.split (COLUMN_SEPARATOR);

            // Create and read record data
            TRecord record = recordSupplier.get ( );
            record.readFromText (columnStrs);

            // Append record to list
            records.add (record);
        }
        while (!line.isEmpty ( ));
    }

    /// Discards stale records
    public void pruneStaleData() {
        // Group the records by id
        Map<Integer, List<TRecord>> groupedById = records
                .stream ( )
                .collect (Collectors.groupingBy (ITextDBRecord::getId));

        // Iterate over each group, preserving only the last record
        for (int key : groupedById.keySet ( )) {
            var items = groupedById.get (key);

            items.sort ((o1, o2) -> o2.getCreatedDate ( ).compareTo (o1.getCreatedDate ( )));

            // Remove all stale records
            for (int i = 0; i < items.size ( ) - 1; i++)
                records.remove (items.get (i));
        }
    }

    /// Saves the record data to file
    public void saveToDisc() throws TextDatabaseWriteException {
        try {
            String salt = String.format("%07d", (int)(Math.random () * 1000000));
            String oldFileName = fileName + "_" + LocalDate.now ( ) + "_" + salt;

            File old = new File (oldFileName);

            File file = new File (fileName);

            if (file.exists () && !file.renameTo (old))
                throw new TextDatabaseWriteException ("Não foi possível fazer backup dos dados.", null);

            BufferedWriter writer = new BufferedWriter (new FileWriter (file));

            for (var record : records) {
                writer.newLine ( );

                String recordStr = String.join (COLUMN_SEPARATOR, record.toText ( ));

                writer.write (recordStr);
            }

            writer.close ();
        } catch (IOException e) {
            throw new TextDatabaseWriteException ("A gravação do banco de dados em texto falhou.", e);
        }
    }

    public void save(TRecord record) {
        this.records.add (record);
    }

    public TRecord get(int id) {
        return getAll ( ).filter (r -> r.getId ( ) == id).findFirst ( ).orElse (null);
    }

    /// Gets all the most recent records
    public Stream<TRecord> getAll() {
        var groupedById = getRecordsGroupedById ( );

        // Return only most recent data
        return groupedById.keySet ( ).stream ( ).map (k -> {
            // Get all records matching id
            var records = groupedById.get (k);

            // Sort by date ascending
            records.sort ((o1, o2) -> o2.getCreatedDate ( ).compareTo (o1.getCreatedDate ( )));

            return records.getLast ( );
        });
    }

    private Map<Integer, List<TRecord>> getRecordsGroupedById() {
        return records.stream ( ).collect (Collectors.groupingBy (ITextDBRecord::getId));
    }
}

