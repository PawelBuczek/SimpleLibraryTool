package com.pawel.model;

public enum ItemType {
    Book(5),
    DVD(3),
    Magazine(4);

    private int borrowLimit;
    ItemType(int borrowLimit){this.borrowLimit = borrowLimit;}

    public void setBorrowLimit(int borrowLimit) {
        this.borrowLimit = borrowLimit;
    }

    public int getBorrowLimit() {
        return borrowLimit;
    }
}
