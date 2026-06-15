package TextDatabases;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class TextDBUtils {
    @SuppressWarnings("unchecked")
    public static <TRecord extends DBRecord, TValue> Function<DBRecord, Object> getColumnValue(Function<TRecord, TValue> getter) {
        return record -> getter.apply((TRecord) record);
    }

    @SuppressWarnings("unchecked")
    public static <TRecord extends DBRecord, TValue> BiConsumer<DBRecord, Object> setColumnValue(BiConsumer<TRecord, TValue> setter) {
        return (record, value) -> setter.accept((TRecord) record, (TValue) value);
    }

    @SuppressWarnings("unchecked")
    public static <TRecord extends DBRecord, TOtherRecord extends DBRecord> Function<DBRecord, DBRecord> getRelationValue(Function<TRecord, TOtherRecord> getter) {
        return record -> getter.apply((TRecord) record);
    }

    @SuppressWarnings("unchecked")
    public static <TRecord extends DBRecord, TOtherRecord extends DBRecord> BiConsumer<DBRecord, DBRecord> setRelationValue(BiConsumer<TRecord, TOtherRecord> setter) {
        return (record, relRecord) -> setter.accept((TRecord) record, (TOtherRecord) relRecord);
    }

    @SuppressWarnings("unchecked")
    public static <TRecord extends DBRecord, TOtherRecord extends DBRecord> Function<DBRecord, List<DBRecord>> getManyRelationList(Function<TRecord, List<TOtherRecord>> getList) {
        return record -> (List<DBRecord>)getList.apply((TRecord) record);
    }
}
