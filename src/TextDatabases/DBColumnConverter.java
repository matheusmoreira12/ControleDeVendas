package TextDatabases;

public abstract class DBColumnConverter {
    public abstract Object convert(String value);
    public abstract String convertBack(Object value);
}
