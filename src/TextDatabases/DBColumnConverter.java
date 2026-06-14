package TextDatabases;

public abstract class DBColumnConverter<T> {
    public abstract T convert(String value);
    public abstract String convertBack(T value);
}
