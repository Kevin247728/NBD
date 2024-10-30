package org.example.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.exceptions.TooManyException;
import org.example.repositories.MgdClientRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Rent extends AbstractEntityMgd {

    @BsonProperty("beginDate")
    private LocalDate beginDate;

    @BsonProperty("endDate")
    private LocalDate endDate;

    @BsonProperty("clientId")
    private UniqueIdMgd clientId;

    @BsonProperty("bookId")
    private UniqueIdMgd bookId;

    @BsonProperty("fee")
    private float fee;

    private MgdClientRepository clientRepository;

    @BsonCreator
    public Rent(@BsonProperty("clientId") UniqueIdMgd clientId,
                @BsonProperty("bookId") UniqueIdMgd bookId,
                @BsonProperty("beginDate") LocalDate beginDate,
                @BsonProperty("endDate") LocalDate endDate,
                MgdClientRepository clientRepository) throws TooManyException {
        super();
        this.clientId = clientId;
        this.bookId = bookId;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.clientRepository = clientRepository;

        validateRent();
        calculateFee();
        getClientById(clientId).addRent(this);
    }

    private void validateRent() throws TooManyException {
        Client client = getClientById(clientId);
        if (client.getBookCount() >= client.getClientType().getMaxBooks()) {
            throw new TooManyException("Client has already rented the maximum number of books.");
        }

        long rentDays = ChronoUnit.DAYS.between(beginDate, endDate);
        if (rentDays > client.getClientType().getMaxRentDays()) {
            throw new TooManyException("Rent duration exceeds the maximum allowed days for this client type.");
        }
    }

    private void calculateFee() {
        Client client = getClientById(clientId);
        if (client.getClientType() instanceof NonStudent) {
            this.fee = ((NonStudent) client.getClientType()).getAdditionalFee();
        }
    }

    public void returnBook() {
        Client client = getClientById(clientId);
        client.removeRent(this);
    }

    private Client getClientById(UniqueIdMgd clientId) {
        return clientRepository.findById(clientId);
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

    public UniqueIdMgd getClientId() {
        return clientId;
    }

    public UniqueIdMgd getBookId() {
        return bookId;
    }
}

