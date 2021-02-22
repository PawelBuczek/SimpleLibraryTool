package com.pawel.model;

import com.pawel.exceptions.IncorrectItemId;
import com.pawel.exceptions.IncorrectUserId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Library {
    private final List<User> userList = new ArrayList<>();
    private final List<Item> itemList = new ArrayList<>();

    public Library() {
    }

    public void borrowItem(int userId, int itemId) throws IncorrectUserId, IncorrectItemId {
        User user = getUserById(userId);
        Item item = getItemById(itemId);

        if (user.borrowItem(item)) {
            System.out.println("Item with id " + itemId + " (title :" + item.getTitle() + ")"
                    + " was successfully borrowed by User with id " + userId + " (name:" + user.getName() + ")");
        } else {
            System.out.println("User already has " + item.getType().getBorrowLimit() + " " + item.getType() + "s."
                    + " Please return one or more before borrowing this one");
        }
        System.out.println("---");
    }

    public void returnItem(int userId, int itemId) throws IncorrectUserId, IncorrectItemId {
        User user = getUserById(userId);
        Item item = getItemById(itemId);
        if (user.returnItem(item)) {
            System.out.println("RETURNED: Item id: " + itemId);
        } else {
            System.out.println("Item not borrowed - cannot be returned");
        }
        System.out.println("---");
    }

    public void addItem(Item item) {
        itemList.add(item);
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public boolean removeItem(Item item) {
        return itemList.remove(item);
    }

    public boolean removeUser(User user) {
        return userList.remove(user);
    }

    private User getUserById(int userId) throws IncorrectUserId {
        Optional<User> optionalUser = userList.stream()
                .filter(i -> i.getUserId() == userId)
                .findAny();
        if (optionalUser.isEmpty()) {
            throw new IncorrectUserId("No user with id: " + userId);
        }
        return optionalUser.get();
    }

    private Item getItemById(int itemId) throws IncorrectItemId {
        Optional<Item> optionalItem = itemList.stream()
                .filter(i -> i.getItemId() == itemId)
                .findAny();
        if (optionalItem.isEmpty()) {
            throw new IncorrectItemId("No item with id: " + itemId);
        }
        return optionalItem.get();
    }

}
