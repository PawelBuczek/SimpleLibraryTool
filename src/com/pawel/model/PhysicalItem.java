package com.pawel.model;

import java.util.Objects;

class PhysicalItem {
    private final int id;
    private final VirtualItem virtualItem;
    private boolean isBorrowed = false;

    public PhysicalItem(int id, VirtualItem virtualItem) {
        this.id = id;
        this.virtualItem = virtualItem;
    }

    public int getId() {
        return id;
    }

    public VirtualItem getVirtualItem() {
        return virtualItem;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhysicalItem that = (PhysicalItem) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PhysicalItem{" +
                "id=" + id +
                ", " + (isBorrowed ? "borrowed" : "in library") +
                '}';
    }
}
