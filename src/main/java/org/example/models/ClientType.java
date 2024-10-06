package org.example.models;

public abstract class ClientType {
    private int maxBooks;
    private int maxRentDays;

    public ClientType(int maxBooks, int maxRentDays) {
        this.maxBooks = maxBooks;
        this.maxRentDays = maxRentDays;
    }

    public int getMaxBooks() {
        return maxBooks;
    }

    public int getMaxRentDays() {
        return maxRentDays;
    }
}

