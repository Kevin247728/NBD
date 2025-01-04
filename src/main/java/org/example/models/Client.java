package org.example.models;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import com.datastax.oss.driver.api.mapper.entity.naming.GetterStyle;

import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_book")
@CqlName("clients")
@PropertyStrategy(mutable = false, getterStyle = GetterStyle.JAVABEANS)
public class Client {

    @PartitionKey
    private UUID id;

    @CqlName("first_name")
    private String firstName;

    @CqlName("last_name")
    private String lastName;

    private String discriminator;

    @CqlName("max_books")
    private int maxBooks;

    @CqlName("max_rent_days")
    private int maxRentDays;

    public Client(String firstName, String lastName, String discriminator, int maxBooks, int maxRentDays) {
        this.id = UUID.randomUUID();
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
