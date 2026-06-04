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

    /**
     * @throws TextDatabaseException when reading fails.
     */
    public void loadFromDisc() throws TextDatabaseException {
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

    private void importRecordsFromTextFile(BufferedReader reader) throws IOException {
        String line;

        while (true) {
            line = reader.readLine();

            if (line == null)
                break; // End was reached. Break!

            if (line.isBlank())
                continue;

            String[] cols = line.split(COLUMN_SEPARATOR);

            TRecord record = recordSupplier.get();
            record.readFromText(cols);

            records.add(record);
        }
    }

    /**
     * Removes old duplicates from the internal storage.
     */
    public void prune() {
        Map<Integer, List<TRecord>> groupedById = getRecordsGroupedById();

        for (var entry : groupedById.entrySet()) {
            var duplicates = entry.getValue();

            duplicates.sort((o1, o2) -> o1.getCreatedDate().compareTo(o2.getCreatedDate()));

            // Delete all old duplicates
            for (int i = 0; i < duplicates.size() - 1; i++)
                records.remove(duplicates.get(i));
        }
    }

    /**
     * @throws TextDatabaseException if saving fails.
     */
    public void saveToDisc() throws TextDatabaseException {
        try {
            String salt = String.format("%07d", (int) (Math.random() * 1000000));
            String oldFileName = fileName + "_" + LocalDateTime.now().format(StaticDefaults.FILE_DATE_TIME_FORMATTER) + "_" + salt;
            File old = new File(oldFileName);

            File file = new File(fileName);

            if (file.exists() && !file.renameTo(old))
                throw new ETextDatabaseBackupFailed();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for (var record : records) {
                String recordStr = String.join(COLUMN_SEPARATOR, record.toText());
                writer.write(recordStr);

                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            throw new ETextDatabaseWriteFailed(e);
        }
    }

    /**
     * @param recordStream the records being added.
     * @return the number of records successfully inserted.
     */
    public long insert(Stream<TRecord> recordStream) {
        return recordStream.map(this::insert)
                .filter(success -> success)
                .count();
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

    /**
     * @param recordStream the records being updated.
     * @return the number of records successfully updated.
     */
    public long update(Stream<TRecord> recordStream) {
        return recordStream.map(this::update)
                .filter(success -> success)
                .count();
    }

    /**
     * @param record the record being updated.
     */
    public boolean update(TRecord record) {
        boolean recordExists = recordExistsForId(record.getId());
        if (!recordExists)
            return false;

        this.records.add(record);

        return true;
    }

    /**
     * @param recordStream the records being inserted or updated.
     */
    public void upsert(Stream<TRecord> recordStream) {
        recordStream.forEach(this::upsert);
    }

    /**
     * @param record the record being inserted or updated.
     */
    public void upsert(TRecord record) {
        this.records.add(record);
    }

    /**
     * @param predicate the selection predicate.
     * @return the matching records.
     */
    public Stream<TRecord> select(Predicate<TRecord> predicate) {
        return getAll().filter(predicate);
    }

    private boolean recordExistsForId(int id) {
        return records.stream()
                .anyMatch(r -> r.getId() == id);
    }

    /**
     * @param id the id of the record being dropped.
     */
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
        return records.stream()
                .collect(Collectors.groupingBy(ITextDBRecord::getId));
    }
}