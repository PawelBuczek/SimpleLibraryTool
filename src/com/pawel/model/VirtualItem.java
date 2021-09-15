package com.pawel.model;

import java.time.Year;
import java.util.Objects;

class VirtualItem {
    private final String ISBN;
    private String title;
    private Year year;
    private String author;
    private ItemType type;

    public VirtualItem(String ISBN, String title, Year year, String author, ItemType type) {
        this.ISBN = ISBN;
        this.title = title;
        this.year = year;
        this.author = author;
        this.type = type;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VirtualItem that = (VirtualItem) o;
        return ISBN.equals(that.ISBN) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ISBN, type);
    }

    @Override
    public String toString() {
        return "VirtualItem{" +
                "ISBN='" + ISBN + '\'' +
                ", title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", author='" + author + '\'' +
                ", type=" + type +
                '}';
    }
}
