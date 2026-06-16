package TextDatabases;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DBColumnDefinition extends DBColumnDefinitionBase {
    private final DBColumnParserFormatter parserFormatter;
    private final Function<DBRecord, Object> accessor;
    private final BiConsumer<DBRecord, Object> modifier;

    public DBColumnDefinition(String key,
                              DBColumnParserFormatter parserFormatter,
                              Type valueType,
                              Function<DBRecord, Object> accessor,
                              BiConsumer<DBRecord, Object> modifier) {
        super(key, valueType);

        this.parserFormatter = parserFormatter;
        this.accessor = accessor;
        this.modifier = modifier;
    }

    public Function<DBRecord, Object> getAccessor() {
        return accessor;
    }

    public BiConsumer<DBRecord, Object> getModifier() {
        return modifier;
    }

    public DBColumnParserFormatter getParserFormatter() {
        return parserFormatter;
    }
}
