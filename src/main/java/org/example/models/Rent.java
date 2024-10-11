package org.example.models;

import jakarta.persistence.*;
import org.example.exceptions.TooManyException;
import org.example.exceptions.UnavailableException;

import java.util.Date;

@Entity
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date beginDate;
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "book_id")
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

    public Rent() {}

    public void returnBook() throws IllegalStateException {
        checkReturnConditions(book);
        
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

    private void checkReturnConditions(Book book) throws IllegalStateException {
        if (!book.isRented()) {
            throw new IllegalStateException("Book is not rented");
        }
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

