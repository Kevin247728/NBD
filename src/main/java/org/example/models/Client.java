package org.example.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.ArrayList;
import java.util.List;


public class Client extends AbstractEntityMgd {

    @BsonProperty("firstName")
    private String firstName;

    @BsonProperty("lastName")
    private String lastName;

    @BsonProperty("clientType")
    private ClientType clientType;

    @BsonProperty("rents")
    private List<Rent> rents = new ArrayList<>();

    @BsonCreator
    public Client(@BsonProperty("firstName") String firstName,
                  @BsonProperty("lastName") String lastName,
                  @BsonProperty("clientType") ClientType clientType) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientType = clientType;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public List<Rent> getRents() {
        return rents;
    }

    public void addRent(Rent rent) {
        rents.add(rent);
    }

    public void removeRent(Rent rent) {
        rents.remove(rent);
    }

    public int getBookCount() {
        return rents.size();
    }
}
