package com.pawel.model;

import com.pawel.exceptions.*;

import java.time.Year;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Library {
    private final Set<User> userSet = new HashSet<>();
    private final Set<VirtualItem> virtualItemSet = new HashSet<>();
    private final Set<PhysicalItem> physicalItemSet = new HashSet<>();
    private static final AtomicInteger physicalItemsCount = new AtomicInteger(0);
    private static final AtomicInteger userCount = new AtomicInteger(0);

    public Library() {
    }

    public int borrowItem(int userId, int itemId) throws IncorrectUserIdException, IncorrectPhysicalItemIdException, CannotBorrowItemException {
        PhysicalItem physicalItem = getPhysicalItemById(itemId);
        if (physicalItem.isBorrowed()) {
            throw new CannotBorrowItemException("Cannot borrow physical item with id: " + itemId + System.lineSeparator() +
                    "This item is already borrowed.");
        }
        User user = getUserById(userId);
        if (user.borrowItem(physicalItem)) {
            return itemId;
        } else {
            throw new CannotBorrowItemException("Cannot borrow physical item with id: " + itemId + System.lineSeparator() +
                    "User already has " + physicalItem.getVirtualItem().getType().getBorrowLimit() + " "
                    + physicalItem.getVirtualItem().getType() + "(s)."
                    + " Please return one or more before borrowing this one.");
        }
    }

    public int borrowItem(int userId, String ISBN) throws IncorrectUserIdException, IncorrectVirtualItemISBNException, CannotBorrowItemException {
        User user = getUserById(userId);
        Optional<VirtualItem> optionalVirtualItem = virtualItemSet.stream()
                .filter(virtualItem -> virtualItem.getISBN().equals(ISBN))
                .findAny();
        if (optionalVirtualItem.isEmpty()) {
            throw new IncorrectVirtualItemISBNException("No virtual item with ISBN: " + ISBN);
        } else {
            return borrowItem(user, optionalVirtualItem.get());
        }
    }

    private int borrowItem(User user, VirtualItem virtualItem) throws CannotBorrowItemException {
        Optional<PhysicalItem> optionalPhysicalItem = getListOfPhysicalCopiesOfVirtualItem(virtualItem).stream()
                .filter(Predicate.not(PhysicalItem::isBorrowed))
                .findFirst();
        if (optionalPhysicalItem.isEmpty()) {
            throw new CannotBorrowItemException("Cannot borrow physical copy of item with ISBN: " + virtualItem.getISBN() +
                    System.lineSeparator() + "No physical copies left in library.");
        } else {
            user.borrowItem(optionalPhysicalItem.get());
            return optionalPhysicalItem.get().getId();
        }
    }

    public void returnItem(int userId, int physicalItemId) throws IncorrectUserIdException, IncorrectPhysicalItemIdException, CannotReturnItemException {
        User user = getUserById(userId);
        PhysicalItem physicalItem = getPhysicalItemById(physicalItemId);
        if (!user.returnItem(physicalItem)) {
            throw new CannotReturnItemException("Error - Item with id: " + physicalItemId
                    + " is not currently borrowed by user with id: " + userId);
        }
    }

    public void removePhysicalItem(int physicalItemId) throws IncorrectPhysicalItemIdException, CannotRemovePhysicalItemException {
        PhysicalItem physicalItem = getPhysicalItemById(physicalItemId);
        if (physicalItem.isBorrowed()) {
            Optional<User> optionalUser = userSet.stream()
                    .filter(user -> user.getBorrowedItems().get(physicalItem.getVirtualItem().getType()).contains(physicalItem))
                    .findAny();
            if (optionalUser.isPresent()) {
                throw new CannotRemovePhysicalItemException("Error - Item with id: " + physicalItemId
                        + " is currently borrowed by user with id: " + optionalUser.get().getUserId());
            } else {
                throw new Error("Something went wrong - Item with id: " + physicalItemId
                        + "is currently borrowed, but user who borrowed it cannot be found.");
            }
        } else {
            physicalItemSet.remove(physicalItem);
        }
    }

    public void removeAllPhysicalCopiesOfVirtualItem(String ISBN) throws IncorrectVirtualItemISBNException, CannotRemovePhysicalItemException {
        VirtualItem virtualItem = getVirtualItemByISBN(ISBN);
        List<PhysicalItem> listOfPhysicalCopiesOfVirtualItem = getListOfPhysicalCopiesOfVirtualItem(virtualItem);
        if (listOfPhysicalCopiesOfVirtualItem.stream().noneMatch(PhysicalItem::isBorrowed)) {
            listOfPhysicalCopiesOfVirtualItem.forEach(item -> {
                try {
                    removePhysicalItem(item.getId());
                } catch (IncorrectPhysicalItemIdException | CannotRemovePhysicalItemException e) {
                    e.printStackTrace();
                }
            });
        } else {
            throw new CannotRemovePhysicalItemException("Error - some physical copies of virtual Item with ISBN: "
                    + virtualItem.getISBN() + " are borrowed.");
        }
    }

    public void removeVirtualItem(String ISBN) throws IncorrectVirtualItemISBNException, CannotRemoveVirtualItemException {
        VirtualItem virtualItem = getVirtualItemByISBN(ISBN);
        if (getListOfPhysicalCopiesOfVirtualItem(virtualItem).isEmpty()) {
            virtualItemSet.remove(virtualItem);
        } else {
            throw new CannotRemoveVirtualItemException("Error - Virtual Item with ISBN: " + virtualItem.getISBN()
                    + " cannot be removed because there are still physical copies of it in the library. Please remove them first.");
        }
    }

    public boolean addVirtualItem(String ISBN, String title, Year year, String author, ItemType type) {
        return virtualItemSet.add(new VirtualItem(ISBN, title, year, author, type));
    }

    public int addVirtualAndPhysicalItem(String ISBN, String title, Year year, String author, ItemType type) throws IncorrectVirtualItemISBNException {
        addVirtualItem(ISBN, title, year, author, type);
        return addPhysicalItem(ISBN);
    }

    public int addPhysicalItem(String ISBN) throws IncorrectVirtualItemISBNException {
        VirtualItem virtualItem = getVirtualItemByISBN(ISBN);
        physicalItemSet.add(new PhysicalItem(physicalItemsCount.incrementAndGet(), virtualItem));
        return physicalItemsCount.get();
    }

    public int addUser(String userName) {
        userSet.add(new User(userCount.incrementAndGet(), userName));
        return userCount.get();
    }

    public boolean removeUser(int userId) throws IncorrectUserIdException, CannotRemoveUserException {
        return removeUser(getUserById(userId));
    }

    private boolean removeUser(User user) throws CannotRemoveUserException {
        if (user.getBorrowedItems().isEmpty()) {
            return userSet.remove(user);
        } else {
            throw new CannotRemoveUserException("User with id: " + user.getUserId() + " cannot be removed." + System.lineSeparator()
                    + "This user still has " + user.getBorrowedItems().size() + " item(s) borrowed.");
        }

    }

    public User getUserById(int userId) throws IncorrectUserIdException {
        Optional<User> optionalUser = userSet.stream()
                .filter(i -> i.getUserId() == userId)
                .findAny();
        if (optionalUser.isEmpty()) {
            throw new IncorrectUserIdException("No user with id: " + userId);
        }
        return optionalUser.get();
    }

    public VirtualItem getVirtualItemByISBN(String ISBN) throws IncorrectVirtualItemISBNException {
        Optional<VirtualItem> optionalItem = virtualItemSet.stream()
                .filter(i -> Objects.equals(i.getISBN(), ISBN))
                .findAny();
        if (optionalItem.isEmpty()) {
            throw new IncorrectVirtualItemISBNException("No virtual item with ISBN: " + ISBN);
        }
        return optionalItem.get();
    }

    public PhysicalItem getPhysicalItemById(int id) throws IncorrectPhysicalItemIdException {
        Optional<PhysicalItem> optionalPhysicalItem = physicalItemSet.stream()
                .filter(physicalItem -> physicalItem.getId() == id)
                .findAny();
        if (optionalPhysicalItem.isEmpty()) {
            throw new IncorrectPhysicalItemIdException("No physical item with itemId: " + id);
        }
        return optionalPhysicalItem.orElse(null);
    }

    public List<PhysicalItem> getListOfPhysicalCopiesOfVirtualItem(VirtualItem virtualItem) {
        Optional<VirtualItem> optionalVirtualItem = virtualItemSet.stream()
                .filter(item -> item.equals(virtualItem))
                .findAny();
        if (optionalVirtualItem.isEmpty()) {
            return new ArrayList<>();
        } else {
            return physicalItemSet.stream()
                    .filter(physicalItem -> physicalItem.getVirtualItem().equals(optionalVirtualItem.get()))
                    .collect(Collectors.toList());
        }
    }

    public List<VirtualItem> searchForVirtualItem(VirtualItemQuery query) {
        Stream<VirtualItem> stream = virtualItemSet.stream();
        if (!query.getTitle().equals("")) {
            stream = stream.filter(item -> item.getTitle().equals(query.getTitle()));
        }
        if (query.getYear() != null) {
            stream = stream.filter(item -> item.getYear().equals(query.getYear()));
        }
        if (!query.getAuthor().equals("")) {
            stream = stream.filter(item -> item.getAuthor().equals(query.getAuthor()));
        }
        if (query.getType() != null) {
            stream = stream.filter(item -> item.getType().equals(query.getType()));
        }
        return stream.collect(Collectors.toList());
    }

    public void printVirtualItemsInLibrary(VirtualItemQuery query) {
        System.out.println("Virtual Items in the library that match passed criteria:");
        searchForVirtualItem(query).forEach(System.out::println);
    }

    public void printAllItemsInLibrary(VirtualItemQuery query) {
        System.out.println("All Items in the library that match passed criteria:");
        searchForVirtualItem(query).forEach(virtualItem -> {
            System.out.println(virtualItem);
            physicalItemSet.stream()
                    .filter(physicalItem -> physicalItem.getVirtualItem().equals(virtualItem))
                    .forEach(physicalItem -> System.out.println("  " + physicalItem));
        });
    }

}
