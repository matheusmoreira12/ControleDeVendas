package TextDatabases;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DBUtils {
    @SuppressWarnings("unchecked")
    public static <TRecord extends DBRecord, TValue> Function<DBRecord, Object> getColumnValue(Function<TRecord, TValue> getter) {
        return record -> getter.apply((TRecord) record);
    }

    @SuppressWarnings("unchecked")
    public static <TRecord extends DBRecord, TValue> BiConsumer<DBRecord, Object> setColumnValue(BiConsumer<TRecord, TValue> setter) {
        return (record, value) -> setter.accept((TRecord) record, (TValue) value);
    }

    @SuppressWarnings("unchecked")
    public static <TRecord extends DBRecord, TRelRecord extends DBRecord> Function<DBRecord, DBRecord> getRelationValue(Function<TRecord, TRelRecord> getter) {
        return record -> getter.apply((TRecord) record);
    }

    @SuppressWarnings("unchecked")
    public static <TRecord extends DBRecord, TRelRecord extends DBRecord> BiConsumer<DBRecord, DBRecord> setRelationValue(BiConsumer<TRecord, TRelRecord> setter) {
        return (record, relRecord) -> setter.accept((TRecord) record, (TRelRecord) relRecord);
    }

    @SuppressWarnings("unchecked")
    public static <TRecord extends DBRecord, TRelRecord extends DBRecord> Function<DBRecord, List<DBRecord>> getManyRelationList(Function<TRecord, List<TRelRecord>> getList) {
        return record -> (List<DBRecord>) getList.apply((TRecord) record);
    }

    public static <TRecord extends DBRecord> Predicate<TRecord> selectMatchingId(int id) {
        return record -> record.getId() == id;
    }

    public static <TRecord extends DBRecord> Predicate<TRecord> selectWithAnyId(int[] ids) {
        return record -> Arrays.stream(ids).anyMatch(id -> record.getId() == id);
    }
}
