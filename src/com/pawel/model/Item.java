package com.pawel.model;

import java.util.Objects;

public class Item {
    private int itemId;
    private String digitalId; //whatever number that can be used to identify two different copies of Book "Wheel of Time"
    private String title;
    private ItemType type;

    public Item(int itemId, String digitalId, String title, ItemType type) {
        this.itemId = itemId;
        this.digitalId = digitalId;
        this.title = title;
        this.type = type;
    }

    public int getItemId() {
        return itemId;
    }

    public String getDigitalId() {
        return digitalId;
    }

    public String getTitle() {
        return title;
    }

    public ItemType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return itemId == item.itemId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}
