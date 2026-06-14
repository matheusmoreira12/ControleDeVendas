package TextDatabases;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class TextDBUtils {
    @SuppressWarnings("unchecked")
    public static <TValue, TRecord extends DBRecord> Function<DBRecord, Object> getColumnValue(Function<TRecord, TValue> getter) {
        return record -> getter.apply((TRecord)record);
    }

    @SuppressWarnings("unchecked")
    public static <TValue, TRecord extends DBRecord> BiConsumer<DBRecord, Object> setColumnValue(BiConsumer<TRecord, TValue> setter) {
        return (record, value) -> setter.accept((TRecord) record, (TValue) value);
    }
}
