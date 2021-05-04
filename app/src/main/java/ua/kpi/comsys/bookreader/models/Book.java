package ua.kpi.comsys.bookreader.models;

import java.util.Comparator;

public class Book implements Comparator<Book> {
    String name, path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int compare(Book o1, Book o2) {
        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
    }
}
