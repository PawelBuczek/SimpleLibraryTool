package com.pawel;

import com.pawel.exceptions.IncorrectItemId;
import com.pawel.exceptions.IncorrectUserId;
import com.pawel.model.Item;
import com.pawel.model.ItemType;
import com.pawel.model.Library;
import com.pawel.model.User;

public class Main {

    public static void main(String[] args) throws IncorrectUserId, IncorrectItemId {
	    Library library = new Library();
        initializeLibrary(library);
        library.borrowItem(0,1);
        library.borrowItem(0,2);
        library.borrowItem(0,3);
        library.borrowItem(0,4);
        library.borrowItem(0,5);
        library.borrowItem(0,6);
        library.borrowItem(0,7);

        library.returnItem(0,3);
        library.borrowItem(0,6);

        library.returnItem(0,9);

        try {
            library.borrowItem(-3,4);
        } catch (IncorrectUserId e) {
            System.out.println(e.getMessage());
        }

    }

    public static void initializeLibrary(Library library) {
        library.addItem(new Item(1,"000","Wheel Of Time", ItemType.Book));
        library.addItem(new Item(2,"000","Wheel Of Time",ItemType.Book));
        library.addItem(new Item(3,"000","Wheel Of Time",ItemType.Book));
        library.addItem(new Item(4,"001","Wheel Of Time (2nd print)",ItemType.Book));
        library.addItem(new Item(5,"002","Java for Juniors",ItemType.Book));
        library.addItem(new Item(6,"002","Java for Juniors",ItemType.Book));
        library.addItem(new Item(7,"003","Horror",ItemType.DVD));
        library.addItem(new Item(8,"004","Comedy",ItemType.DVD));
        library.addItem(new Item(9,"005","Romance",ItemType.DVD));
        library.addItem(new Item(10,"005","Romance",ItemType.DVD));
        library.addItem(new Item(11,"006","CODE",ItemType.Magazine));

        library.addUser(new User(0,"Pawel"));
        library.addUser(new User(1,"Lee"));
    }
}
