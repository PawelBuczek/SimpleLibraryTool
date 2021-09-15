package com.pawel.model;

import com.pawel.exceptions.CannotBorrowItemException;

import java.util.*;

class User {
    private final int userId;
    private String name;
    private final Map<ItemType, List<PhysicalItem>> borrowedItems = new HashMap<>();

    public User(int userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public boolean borrowItem(PhysicalItem physicalItem) throws CannotBorrowItemException {
        ItemType type = physicalItem.getVirtualItem().getType();
        borrowedItems.computeIfAbsent(type, k -> new ArrayList<>());
        if (borrowedItems.get(type).size() < type.getBorrowLimit()) {
            borrowedItems.get(type).add(physicalItem);
            physicalItem.setBorrowed(true);
            System.out.println("Item with id " + physicalItem.getId() + " (title :" + physicalItem.getVirtualItem().getTitle() + ")"
                    + " (ISBN :" + physicalItem.getVirtualItem().getISBN() + ")"
                    + " was successfully borrowed by User with id " + userId + " (name:" + this.getName() + ")");
            return true;
        } else {
            throw new CannotBorrowItemException("Item with id: " + physicalItem.getId() + " cannot be borrowed by User with id: " + userId
                    + System.lineSeparator() + "This user has already reached limit of " + type.getBorrowLimit() + " borrowed " + type.name() + "s.");
        }
    }

    public boolean returnItem(PhysicalItem physicalItem) {
        ItemType type = physicalItem.getVirtualItem().getType();
        List<PhysicalItem> borrowedItemsOfGivenType = borrowedItems.get(type);
        if (borrowedItemsOfGivenType == null) {
            return false;
        }
        Optional<PhysicalItem> itemToReturn = borrowedItemsOfGivenType.stream()
                .filter(i -> i.equals(physicalItem))
                .findAny();
        if (itemToReturn.isPresent()) {
            borrowedItemsOfGivenType.remove(physicalItem);
            physicalItem.setBorrowed(false);
            System.out.println("Item with id " + physicalItem.getId() + " (title :" + physicalItem.getVirtualItem().getTitle() + ")"
                    + " (ISBN :" + physicalItem.getVirtualItem().getISBN() + ")"
                    + " was successfully returned by User with id " + userId + " (name:" + this.getName() + ")");
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

    public void setName(String name) {
        this.name = name;
    }

    public Map<ItemType, List<PhysicalItem>> getBorrowedItems() {
        return borrowedItems;
    }

}
