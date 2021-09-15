package com.pawel;

import com.pawel.exceptions.*;
import com.pawel.model.ItemType;
import com.pawel.model.Library;
import com.pawel.model.VirtualItemQueryBuilder;

import java.time.Year;

public class ExampleOfUsage {
    private static int exemplaryItemId = 0;
    private static int pawelId = 0;
    private static int leeId = 0;

    public static void main(String[] args) throws IncorrectUserIdException, IncorrectPhysicalItemIdException, CannotBorrowItemException, IncorrectVirtualItemISBNException, CannotReturnItemException {
        Library library = new Library();
        initializeLibrary(library);

        library.borrowItem(pawelId, exemplaryItemId);
        System.out.println("---");

        int borrowedItemId1 = library.borrowItem(pawelId, "0001-A");
        library.borrowItem(pawelId, "0001-A");
        library.borrowItem(pawelId, "0001-A");
        library.borrowItem(pawelId, "0001-A");
        System.out.println("---");

        int borrowedItemId2 = library.borrowItem(leeId, "0002");
        System.out.println("---");

        library.returnItem(leeId, borrowedItemId2);
        System.out.println("---");

        System.out.println("Possible exceptions:");
        try {
            library.borrowItem(-3123, "0002");
        } catch (IncorrectUserIdException e) {
            System.out.println(e.getMessage());
        }
        try {
            library.borrowItem(leeId, "0009");
        } catch (IncorrectVirtualItemISBNException e) {
            System.out.println(e.getMessage());
        }
        try {
            library.borrowItem(leeId, "0003");
        } catch (CannotBorrowItemException e) {
            System.out.println(e.getMessage());
        }
        try {
            library.borrowItem(leeId, 12333);
        } catch (IncorrectPhysicalItemIdException e) {
            System.out.println(e.getMessage());
        }
        try {
            library.returnItem(leeId, borrowedItemId2);
        } catch (CannotReturnItemException e) {
            System.out.println(e.getMessage());
        }
        try {
            library.removePhysicalItem(borrowedItemId1);
        } catch (CannotRemovePhysicalItemException e) {
            System.out.println(e.getMessage());
        }
        try {
            library.removeAllPhysicalCopiesOfVirtualItem("0001-A");
        } catch (CannotRemovePhysicalItemException e) {
            System.out.println(e.getMessage());
        }
        try {
            library.removeVirtualItem("0001-A");
        } catch (CannotRemoveVirtualItemException e) {
            System.out.println(e.getMessage());
        }
        try {
            library.removeUser(leeId);
        } catch (CannotRemoveUserException e) {
            System.out.println(e.getMessage());
        }
        try {
            library.borrowItem(pawelId, "0004");
        } catch (CannotBorrowItemException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("---");

        library.printVirtualItemsInLibrary(
                new VirtualItemQueryBuilder().setAuthor("Robert Jordan").setYear(Year.of(1993)).build());
        library.printVirtualItemsInLibrary(
                new VirtualItemQueryBuilder().setType(ItemType.DVD).build());
        library.printAllItemsInLibrary(new VirtualItemQueryBuilder().build());

    }

    public static void initializeLibrary(Library library) throws IncorrectVirtualItemISBNException {
        library.addVirtualItem("0001-A", "Wheel Of Time", Year.of(1993), "Robert Jordan", ItemType.Book);
        library.addPhysicalItem("0001-A");
        library.addPhysicalItem("0001-A");
        library.addPhysicalItem("0001-A");
        library.addPhysicalItem("0001-A");
        exemplaryItemId = library.addPhysicalItem("0001-A");
        library.addVirtualAndPhysicalItem("0001-B", "Wheel Of Time (2nd print)", Year.of(2002), "Robert Jordan", ItemType.Book);
        library.addVirtualAndPhysicalItem("0002", "Java for Juniors", Year.of(2015), "smart guy", ItemType.Book);
        library.addVirtualAndPhysicalItem("0002", "Java for Juniors", Year.of(2015), "smart guy", ItemType.Book);
        library.addVirtualItem("0003", "Horror", Year.of(2011), "horrorWriter", ItemType.Book);  //virtual item is added, but no physical copies available
        library.addVirtualAndPhysicalItem("0004", "Comedy", Year.of(1993), "Robert Jordan", ItemType.Book);
        library.addVirtualAndPhysicalItem("0005", "Romance", Year.of(1991), "romanceFilmMaker", ItemType.DVD);
        library.addVirtualAndPhysicalItem("0006", "CODE", Year.of(1990), "codeMagWriter", ItemType.Magazine);
        library.addPhysicalItem("0006");

        pawelId = library.addUser("Pawel");
        leeId = library.addUser("Lee");
    }
}
