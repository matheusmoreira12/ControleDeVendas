package TextDatabases;

import TextDatabases.Exceptions.TextDatabaseBackupFailedException;
import TextDatabases.Exceptions.ETextDatabaseReadFailed;
import TextDatabases.Exceptions.ETextDatabaseWriteFailed;
import TextDatabases.Exceptions.TextDatabaseException;

import java.io.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static TextDatabases.StaticDefaults.COLUMN_SEPARATOR;
import static TextDatabases.StaticDefaults.FILE_DATE_TIME_FORMATTER;

/// Allows the manipulation of TSV text databases
public class TextDatabase<TRecord extends TextDBRecord> {
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
            throw new ETextDatabaseReadFailed(e);
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

            TRecord record = recordSupplier.get();

            LinkedList<String> columns = new LinkedList<>(Arrays.asList(line.split(COLUMN_SEPARATOR)));
            record.deserialize(columns);

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

            duplicates.sort(Comparator.comparing(TextDBRecord::getCreatedDate));

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
            OffsetDateTime nowUtc = OffsetDateTime.now(ZoneOffset.UTC);
            String oldFileName = fileName + "_" + nowUtc.format(FILE_DATE_TIME_FORMATTER) + "_" + salt;
            File old = new File(oldFileName);

            File file = new File(fileName);

            if (file.exists() && !file.renameTo(old))
                throw new TextDatabaseBackupFailedException();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for (var record : records) {
                ArrayList<String> columns = new ArrayList<>();
                record.serialize(columns);

                String recordStr = String.join(COLUMN_SEPARATOR, columns);
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
                .collect(Collectors.groupingBy(TextDBRecord::getId));
    }
}