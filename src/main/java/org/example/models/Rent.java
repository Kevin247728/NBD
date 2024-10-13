package org.example.models;

import jakarta.persistence.*;
import org.example.exceptions.TooManyException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
public class Rent extends AbstractEntity {

    private LocalDate beginDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    private float fee = 0;

    public Rent(Client client, Book book, LocalDate beginDate, LocalDate endDate) throws TooManyException {
        this.client = client;
        this.book = book;
        this.beginDate = beginDate;
        this.endDate = endDate;

        validateRent();
        calculateFee();
        client.addRent(this);
    }

    public Rent() {}


    private void validateRent() throws TooManyException {
        if (client.getBookCount() >= client.getClientType().getMaxBooks()) {
            throw new TooManyException("Client has already rented the maximum number of books.");
        }

        long rentDays = ChronoUnit.DAYS.between(beginDate, endDate);
        if (rentDays > client.getClientType().getMaxRentDays()) {
            throw new TooManyException("Rent duration exceeds the maximum allowed days for this client type.");
        }
    }

    private void calculateFee() {
        if (client.getClientType() instanceof NonStudent) {
            this.fee = ((NonStudent) client.getClientType()).getAdditionalFee();
        }
    }

    public void returnBook() {
        client.removeRent(this);
    }

    public float getFee() {
        return fee;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Client getClient() {
        return client;
    }

    public Book getBook() {
        return book;
    }
}

