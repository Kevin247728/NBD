package org.example.models;

import org.example.exceptions.TooManyException;
import org.example.exceptions.UnavailableException;

import java.util.Date;

public class Rent {
    private int id;
    private Date beginDate;
    private Date endDate;
    private Client client;
    private Book book;
    private float fee;

    public Rent(int id, Client client, Book book, Date beginDate, float fee) throws UnavailableException, TooManyException {
        checkRentConditions(client, book);

        book.rentBook();
        client.addRent(this);

        this.id = id;
        this.client = client;
        this.book = book;
        this.beginDate = beginDate;
        this.fee = fee;
    }

    public void returnBook() {
        book.returnBook();
        client.removeRent(this);
    }

    public float getFee() {
        return fee;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Client getClient() {
        return client;
    }

    public Book getBook() {
        return book;
    }

    private void checkRentConditions(Client client, Book book) throws UnavailableException, TooManyException {
        if (book.isRented()) {
            throw new UnavailableException("The book is already rented.");
        }

        if (client.getRents().size() >= client.getClientType().getMaxBooks()) {
            throw new TooManyException("Client cannot rent more books.");
        }
    }
}

