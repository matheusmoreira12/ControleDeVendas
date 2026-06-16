package TextDatabases;

public abstract class DBColumnParserFormatter {
    public abstract Object parse(String value);
    public abstract String format(Object value);
}
