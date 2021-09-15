package com.pawel.model;

import com.pawel.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {
    private static Library library;

    @BeforeEach
    public void beforeEach() {
        library = new Library();
        library.addVirtualItem("A", "Wheel Of Time", Year.of(1993), "Robert Jordan", ItemType.DVD);
    }

    @Test
    void addVirtualItemDuplicateISBN() {
        assertFalse(library.addVirtualItem("A", "AAA", Year.of(2000), "Me", ItemType.Book));
        assertEquals("Wheel Of Time", assertDoesNotThrow(() -> library.getVirtualItemByISBN("A").getTitle()));
    }

    @Test
    void addPhysicalItem() {
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
    }

    @Test
    void addPhysicalItemWrongISBN() {
        assertThrows(IncorrectVirtualItemISBNException.class, () -> library.addPhysicalItem("B"));
    }

    @Test
    void addVirtualAndPhysicalItemExisting() {
        assertDoesNotThrow(() -> library.addVirtualAndPhysicalItem("A", "Wheel Of Time", Year.of(1993), "Robert Jordan", ItemType.DVD));
    }

    @Test
    void addVirtualAndPhysicalItemNotExisting() {
        assertDoesNotThrow(() -> library.addVirtualAndPhysicalItem("B", "Book", Year.of(2001), "Robert Jordan", ItemType.Book));
        assertEquals("Book", assertDoesNotThrow(() -> library.getVirtualItemByISBN("B")).getTitle());
        assertEquals(1, assertDoesNotThrow(()
                -> library.getListOfPhysicalCopiesOfVirtualItem(library.getVirtualItemByISBN("B"))).size());
    }

    @Test
    void borrowExistingItemProvidingId() {
        int userId = library.addUser("User");
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.borrowItem(userId, physicalItemId));
    }

    @Test
    void borrowExistingItemProvidingISBN() {
        int userId = library.addUser("User");
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.borrowItem(userId, "A"));
    }

    @Test
    void borrowItemWrongUser() {
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertThrows(IncorrectUserIdException.class, () -> library.borrowItem(321, physicalItemId));
    }

    @Test
    void borrowItemWrongId() {
        int userId = library.addUser("User");
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertThrows(IncorrectPhysicalItemIdException.class, () -> library.borrowItem(userId, -321));
    }

    @Test
    void borrowItemWrongISBN() {
        int userId = library.addUser("User");
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertThrows(IncorrectVirtualItemISBNException.class, () -> library.borrowItem(userId, "BAD"));
    }

    @Test
    void borrowItemAlreadyBorrowedByTheSameUser() {
        int userId = library.addUser("User");
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.borrowItem(userId, physicalItemId));
        assertThrows(CannotBorrowItemException.class, () -> library.borrowItem(userId, physicalItemId));
    }

    @Test
    void borrowItemAlreadyBorrowedByADifferentUser() {
        int userId = library.addUser("User");
        int differentUserId = library.addUser("DifferentUser");
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.borrowItem(userId, physicalItemId));
        assertThrows(CannotBorrowItemException.class, () -> library.borrowItem(differentUserId, physicalItemId));
    }

    @Test
    void returnItem() {
        int userId = library.addUser("User");
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.borrowItem(userId, physicalItemId));
        assertDoesNotThrow(() -> library.returnItem(userId, physicalItemId));
    }

    @Test
    void returnItemWrongId() {
        int userId = library.addUser("User");
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.borrowItem(userId, physicalItemId));
        assertThrows(IncorrectPhysicalItemIdException.class, () -> library.returnItem(userId, -321));
    }

    @Test
    void returnItemWrongUser() {
        int userId = library.addUser("User");
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.borrowItem(userId, physicalItemId));
        assertThrows(IncorrectUserIdException.class, () -> library.returnItem(-321, physicalItemId));
    }

    @Test
    void returnItemDifferentUser() {
        int userId = library.addUser("User");
        int differentUserId = library.addUser("DifferentUser");
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.borrowItem(userId, physicalItemId));
        assertThrows(CannotReturnItemException.class, () -> library.returnItem(differentUserId, physicalItemId));
    }

    @Test
    void returnItemNotBorrowed() {
        int userId = library.addUser("User");
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertThrows(CannotReturnItemException.class, () -> library.returnItem(userId, physicalItemId));
    }

    @Test
    void getUserThatBorrowedItemWithIdExistingBorrowed() {
        int userId = library.addUser("User");
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.borrowItem(userId, physicalItemId));
        assertEquals(userId, assertDoesNotThrow(() -> library.getUserThatBorrowedItemWithId(physicalItemId).getUserId()));
    }

    @Test
    void getUserThatBorrowedItemWithIdExistingNotBorrowed() {
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertNull(assertDoesNotThrow(() -> library.getUserThatBorrowedItemWithId(physicalItemId)));
    }

    @Test
    void getUserThatBorrowedItemWithIdNotExisting() {
        int userId = library.addUser("User");
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.borrowItem(userId, physicalItemId));
        assertThrows(IncorrectPhysicalItemIdException.class, () -> library.getUserThatBorrowedItemWithId(-321));
    }

    @Test
    void removePhysicalItem() {
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.removePhysicalItem(physicalItemId));
    }

    @Test
    void removePhysicalItemNotExisting() {
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertThrows(IncorrectPhysicalItemIdException.class, () -> library.removePhysicalItem(-321));
    }

    @Test
    void removePhysicalItemBorrowed() {
        int userId = library.addUser("User");
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.borrowItem(userId, physicalItemId));
        assertThrows(CannotRemovePhysicalItemException.class, () -> library.removePhysicalItem(physicalItemId));
    }

    @Test
    void removeAllPhysicalCopiesOfVirtualItem() {
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertEquals(3, library.getListOfPhysicalCopiesOfVirtualItem(
                assertDoesNotThrow(() -> library.getVirtualItemByISBN("A"))).size());
        assertDoesNotThrow(() -> library.removeAllPhysicalCopiesOfVirtualItem("A"));
        assertEquals(0, library.getListOfPhysicalCopiesOfVirtualItem(
                assertDoesNotThrow(() -> library.getVirtualItemByISBN("A"))).size());
    }

    @Test
    void removeAllPhysicalCopiesOfVirtualItemWrongISBN() {
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertEquals(3, library.getListOfPhysicalCopiesOfVirtualItem(
                assertDoesNotThrow(() -> library.getVirtualItemByISBN("A"))).size());
        assertThrows(IncorrectVirtualItemISBNException.class, () -> library.removeAllPhysicalCopiesOfVirtualItem("B"));
    }

    @Test
    void removeAllPhysicalCopiesOfVirtualItemAnyBorrowed() {
        int userId = library.addUser("User");
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertEquals(3, library.getListOfPhysicalCopiesOfVirtualItem(
                assertDoesNotThrow(() -> library.getVirtualItemByISBN("A"))).size());
        assertDoesNotThrow(() -> library.borrowItem(userId, physicalItemId));
        assertThrows(CannotRemovePhysicalItemException.class, () -> library.removeAllPhysicalCopiesOfVirtualItem("A"));
        assertEquals(3, library.getListOfPhysicalCopiesOfVirtualItem(
                assertDoesNotThrow(() -> library.getVirtualItemByISBN("A"))).size());
    }

    @Test
    void removeVirtualItem() {
        assertDoesNotThrow(() -> library.removeVirtualItem("A"));
    }

    @Test
    void removeVirtualItemWrongISBN() {
        assertThrows(IncorrectVirtualItemISBNException.class, () -> library.removeVirtualItem("B"));
    }

    @Test
    void removeVirtualItemWithPhysicalCopies() {
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertThrows(CannotRemoveVirtualItemException.class, () -> library.removeVirtualItem("A"));
    }

    @Test
    void removeUser() {
        int userId = library.addUser("User");
        assertDoesNotThrow(() -> library.removeUser(userId));
    }

    @Test
    void removeUserWrongId() {
        library.addUser("User");
        assertThrows(IncorrectUserIdException.class, () -> library.removeUser(-321));
    }

    @Test
    void getUserById() {
        int userId = library.addUser("User");
        assertDoesNotThrow(() -> library.getUserById(userId));
    }

    @Test
    void getUserByIdWrongId() {
        library.addUser("User");
        assertThrows(IncorrectUserIdException.class, () -> library.getUserById(-321));
    }

    @Test
    void getVirtualItemByISBN() {
        assertDoesNotThrow(() -> library.getVirtualItemByISBN("A"));
    }

    @Test
    void getVirtualItemByISBNWrongISBN() {
        assertThrows(IncorrectVirtualItemISBNException.class, () -> library.getVirtualItemByISBN("BAD"));
    }

    @Test
    void getPhysicalItemById() {
        int physicalItemId = assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.getPhysicalItemById(physicalItemId));
    }

    @Test
    void getPhysicalItemByIdWrongId() {
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertThrows(IncorrectPhysicalItemIdException.class, () -> library.getPhysicalItemById(-321));
    }

    @Test
    void getListOfPhysicalCopiesOfVirtualItem() {
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertDoesNotThrow(() -> library.addPhysicalItem("A"));
        assertEquals(2, assertDoesNotThrow(() ->
                library.getListOfPhysicalCopiesOfVirtualItem(library.getVirtualItemByISBN("A"))).size());
    }

    @Test
    void getListOfPhysicalCopiesOfVirtualItemEmpty() {
        assertEquals(0, assertDoesNotThrow(() ->
                library.getListOfPhysicalCopiesOfVirtualItem(library.getVirtualItemByISBN("A"))).size());
    }

    @Test
    void getListOfPhysicalCopiesOfVirtualItemNotExisting() {
        //normally creating a new virtualItem outside of library should not be possible, since that class is package-private
        //just checking method behaviour in this hypothetical scenario
        VirtualItem virtualItem = new VirtualItem("B", "BookNotInLibrary", Year.of(2001), "Me", ItemType.Book);
        assertEquals(0, assertDoesNotThrow(() ->
                library.getListOfPhysicalCopiesOfVirtualItem(virtualItem)).size());
    }

    @Test
    void searchForVirtualItemNoCriteria() {
        library.addVirtualItem("B", "Book", Year.of(2000), "Me", ItemType.Book);
        assertEquals(2, library.searchForVirtualItem(new VirtualItemQueryBuilder().build()).size());
    }

    @Test
    void searchForVirtualItemOneCriteria() {
        library.addVirtualItem("B", "Book", Year.of(2000), "Me", ItemType.Book);
        library.addVirtualItem("C", "Meh", Year.of(1998), "Los", ItemType.Book);
        assertEquals(2, library.searchForVirtualItem(
                new VirtualItemQueryBuilder().setType(ItemType.Book).build()).size());
    }

    @Test
    void searchForVirtualItemTwoCriteria() {
        library.addVirtualItem("B", "Book", Year.of(2000), "Me", ItemType.Book);
        library.addVirtualItem("C", "Meh", Year.of(1998), "Los", ItemType.Book);
        assertEquals(1, library.searchForVirtualItem(
                new VirtualItemQueryBuilder().setType(ItemType.Book).setYear(Year.of(2000)).build()).size());
    }

    @Test
    void searchForVirtualItemTwoCriteriaNoResult() {
        library.addVirtualItem("B", "Book", Year.of(2000), "Me", ItemType.Book);
        library.addVirtualItem("C", "Meh", Year.of(1998), "Los", ItemType.Book);
        assertEquals(0, library.searchForVirtualItem(
                new VirtualItemQueryBuilder().setType(ItemType.Book).setYear(Year.of(1993)).build()).size());
    }

}