package com.pawel.model;

import java.time.Year;

public class VirtualItemQueryBuilder {
    private String title = "";
    private Year year;
    private String author = "";
    private ItemType type;

    public VirtualItemQueryBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public VirtualItemQueryBuilder setYear(Year year) {
        this.year = year;
        return this;
    }

    public VirtualItemQueryBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public VirtualItemQueryBuilder setType(ItemType type) {
        this.type = type;
        return this;
    }

    public VirtualItemQuery build() {
        return new VirtualItemQuery(title, year, author, type);
    }
}