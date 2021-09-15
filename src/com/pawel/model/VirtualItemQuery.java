package com.pawel.model;

import java.time.Year;

public class VirtualItemQuery {
    private final String title;
    private final Year year;
    private final String author;
    private final ItemType type;

    public VirtualItemQuery(String title, Year year, String author, ItemType type) {
        this.title = title;
        this.year = year;
        this.author = author;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public Year getYear() {
        return year;
    }

    public String getAuthor() {
        return author;
    }

    public ItemType getType() {
        return type;
    }
}
