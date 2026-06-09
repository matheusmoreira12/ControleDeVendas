package Printers;

import java.util.Enumeration;

public interface ITabularDataProvider {
    Enumeration<Enumeration<String>> getTabularData();
}
