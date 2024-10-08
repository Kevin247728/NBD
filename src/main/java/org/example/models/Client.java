package org.example.models;

import org.example.exceptions.TooManyException;
import org.example.exceptions.UnavailableException;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private String firstName;
    private String lastName;
    private int clientID;
    private ClientType clientType;
    private List<Rent> rents = new ArrayList<>();
    private int bookCount;

    public Client(String firstName, String lastName, int clientID, ClientType clientType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientID = clientID;
        this.clientType = clientType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getClientID() {
        return clientID;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public List<Rent> getRents() {
        return rents;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void addRent(Rent rent) throws TooManyException {
        rents.add(rent);
        bookCount++;
    }

    public void removeRent(Rent rent) {
        rents.remove(rent);
        bookCount--;
    }
}
