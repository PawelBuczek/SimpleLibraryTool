package com.pawel.model;

import com.pawel.exceptions.CannotBorrowItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    //normally creating these classes outside of library should not be possible, since they are package-private
    private static User user;
    private static VirtualItem virtualItem;
    private static PhysicalItem physicalItem;

    @BeforeEach
    public void beforeEach() {
        user = new User(1, "User");
        virtualItem = new VirtualItem("A", "Wheel Of Time", Year.of(1993), "Robert Jordan", ItemType.DVD);
        physicalItem = new PhysicalItem(1, virtualItem);
    }


    @Test
    void borrowItem() {
        assertDoesNotThrow(() -> user.borrowItem(physicalItem));
    }

    @Test
    void borrowItemAlreadyBorrowedBySameUser() {
        assertDoesNotThrow(() -> user.borrowItem(physicalItem));
        assertThrows(CannotBorrowItemException.class, () -> user.borrowItem(physicalItem));
    }

    @Test
    void borrowItemAlreadyBorrowedByDifferentUser() {
        User differentUser = new User(2, "DifferentUser");
        assertDoesNotThrow(() -> differentUser.borrowItem(physicalItem));
        assertThrows(CannotBorrowItemException.class, () -> user.borrowItem(physicalItem));
    }

    @Test
    void returnItem() {
        assertDoesNotThrow(() -> user.borrowItem(physicalItem));
        assertTrue(assertDoesNotThrow(() -> user.returnItem(physicalItem)));
    }

    @Test
    void returnItemNotBorrowed() {
        assertFalse(assertDoesNotThrow(() -> user.returnItem(physicalItem)));
    }

    @Test
    void returnItemBorrowedByDifferentUser() {
        User differentUser = new User(2, "DifferentUser");
        assertDoesNotThrow(() -> differentUser.borrowItem(physicalItem));
        assertFalse(assertDoesNotThrow(() -> user.returnItem(physicalItem)));
    }

    @Test
    void getBorrowedItems() {
        assertDoesNotThrow(() -> user.borrowItem(physicalItem));
        PhysicalItem differentItem = new PhysicalItem(2, virtualItem);
        assertDoesNotThrow(() -> user.borrowItem(differentItem));
        assertEquals(1, user.getBorrowedItems().size());  //map
        assertEquals(2, user.getBorrowedItems().get(ItemType.DVD).size());  //list
    }
}