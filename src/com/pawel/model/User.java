package com.pawel.model;

import com.pawel.model.Item;
import com.pawel.model.ItemType;

import java.util.*;

public class User {
    private int userId;
    private String name;
    private Map<ItemType, List<Item>> borrowedItems = new HashMap<>();

    public User(int userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public boolean borrowItem(Item item) {
        ItemType type = item.getType();
        borrowedItems.computeIfAbsent(type, k -> new ArrayList<>());
        if (borrowedItems.get(type).size() < type.getBorrowLimit()) {
            borrowedItems.get(type).add(item);
            return true;
        } else {
            return false;
        }
    }

    public boolean returnItem(Item item) {
        ItemType type = item.getType();
        List<Item> borrowedItemsOfGivenType = borrowedItems.get(type);
        if (borrowedItemsOfGivenType == null) {
            return false;
        }
        Optional<Item> itemToReturn = borrowedItemsOfGivenType.stream()
                .filter(i -> i.equals(item))
                .findAny();
        if (itemToReturn.isPresent()) {
            borrowedItemsOfGivenType.remove(item);
            return true;
        } else {
            return false;
        }
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Map<ItemType, List<Item>> getBorrowedItems() {
        return borrowedItems;
    }


}
