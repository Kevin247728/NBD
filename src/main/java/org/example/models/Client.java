package org.example.models;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.entity.naming.GetterStyle;
import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_book")
@CqlName("clients")
@PropertyStrategy(mutable = false, getterStyle = GetterStyle.JAVABEANS)
public class Client {

    @PartitionKey
    protected UUID id;

    @CqlName("first_name")
    protected String firstName;

    @CqlName("last_name")
    protected String lastName;

    @ClusteringColumn
    protected String discriminator;

    @CqlName("max_books")
    protected int maxBooks;

    @CqlName("max_rent_days")
    protected int maxRentDays;

    public Client(String firstName, String lastName, String discriminator, int maxBooks, int maxRentDays) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.discriminator = discriminator;
        this.maxBooks = maxBooks;
        this.maxRentDays = maxRentDays;
    }

    public Client(UUID id, String firstName, String lastName, String discriminator, int maxBooks, int maxRentDays) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.discriminator = discriminator;
        this.maxBooks = maxBooks;
        this.maxRentDays = maxRentDays;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public int getMaxBooks() {
        return maxBooks;
    }

    public int getMaxRentDays() {
        return maxRentDays;
    }

    public UUID getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", discriminator='" + discriminator + '\'' +
                ", maxBooks=" + maxBooks +
                ", maxRentDays=" + maxRentDays +
                '}';
    }
}
