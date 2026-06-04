package TextDatabases;

import TextDatabases.Exceptions.ETextDatabaseBackupFailed;
import TextDatabases.Exceptions.ETextDatabaseReadFailed;
import TextDatabases.Exceptions.ETextDatabaseWriteFailed;
import TextDatabases.Exceptions.TextDatabaseException;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/// Allows the manipulation of TSV text databases
public class TextDatabase<TRecord extends ITextDBRecord> {
    static private final String COLUMN_SEPARATOR = "\t";

    private final String fileName;

    private final ArrayList<TRecord> records;

    private final Supplier<TRecord> recordSupplier;

    public TextDatabase(String fileName, Supplier<TRecord> recordSupplier) {
        this.fileName = fileName;
        this.recordSupplier = recordSupplier;
        this.records = new ArrayList<>();
    }

    /// Loads all records from a file, replacing old data with new data
    public void loadFromDisc() throws ETextDatabaseReadFailed {
        File file = new File(fileName);

        try {
            if (!file.exists())
                return; // Nothing to read. Return!

            BufferedReader reader = new BufferedReader(new FileReader(file));

            importRecordsFromTextFile(reader);

            reader.close();
        } catch (IOException e) {
            throw new ETextDatabaseReadFailed("A leitura do banco de dados em texto falhou.", e);
        }
    }

    /// Imports all records from a file
    private void importRecordsFromTextFile(BufferedReader reader) throws IOException {
        String line;

        while(true) {
            // Read line
            line = reader.readLine();

            if (line == null)
                break; // End was reached. Break!

            // Parse record data
            String[] cols = line.split(COLUMN_SEPARATOR);

            // Create and read record data
            TRecord record = recordSupplier.get();
            record.readFromText(cols);

            // Append record to list
            records.add(record);
        }
    }

    /// Discards stale records
    public void pruneStaleData() {
        // Group the records by id
        Map<Integer, List<TRecord>> groupedById = records
                .stream()
                .collect(Collectors.groupingBy(ITextDBRecord::getId));

        // Iterate over each group, preserving only the last record
        for (int key : groupedById.keySet()) {
            var items = groupedById.get(key);

            items.sort((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()));

            // Remove all stale records
            for (int i = 0; i < items.size() - 1; i++)
                records.remove(items.get(i));
        }
    }

    /// Saves the record data to file
    public void saveToDisc() throws TextDatabaseException {
        try {
            String salt = String.format("%07d", (int) (Math.random() * 1000000));
            String oldFileName = fileName + "_" + LocalDateTime.now().format(StaticDefaults.FILE_DATE_TIME_FORMATTER) + "_" + salt;
            File old = new File(oldFileName);

            File file = new File(fileName);

            if (file.exists() && !file.renameTo(old))
                throw new ETextDatabaseBackupFailed();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            boolean isFirstLine = true;

            for (var record : records) {
                if (!isFirstLine)
                    writer.newLine();

                String recordStr = String.join(COLUMN_SEPARATOR, record.toText());

                writer.write(recordStr);

                isFirstLine = false;
            }

            writer.close();
        } catch (IOException e) {
            throw new ETextDatabaseWriteFailed(e);
        }
    }

    public long insert(Stream<TRecord> recordStream) {
        // Insert each record, counting the number of successful updates
        return recordStream.map(this::insert).filter(s -> s).count();
    }

    /**
     * @param record the record being inserted
     * @return true, if successful.
     */
    public boolean insert(TRecord record) {
        boolean hasConflicts = recordExistsForId(record.getId());
        if (hasConflicts)
            return false;

        this.records.add(record);

        return false;
    }

    public long update(Stream<TRecord> records) {
        // Update each record, counting the number of successful updates
        return records.map(this::update).filter(s -> s).count();
    }

    public boolean update(TRecord record) {
        boolean recordExists = recordExistsForId(record.getId());
        if (!recordExists)
            return false;

        this.records.add(record);

        return true;
    }

    public Stream<TRecord> select(Predicate<TRecord> predicate) {
        return getAll().filter(predicate);
    }

    private boolean recordExistsForId(int id) {
        return records.stream().anyMatch(r -> r.getId() == id);
    }

    public void drop(int id) {
        this.records.removeIf(r -> r.getId() == id);
    }

    private Stream<TRecord> getAll() {
        var groupedById = getRecordsGroupedById();

        // Return only most recent data
        return groupedById.keySet().stream().map(k -> {
            // Get all records matching id
            var records = groupedById.get(k);

            // Sort by date ascending
            records.sort((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()));

            return records.getLast();
        });
    }

    private Map<Integer, List<TRecord>> getRecordsGroupedById() {
        return records.stream().collect(Collectors.groupingBy(ITextDBRecord::getId));
    }
}