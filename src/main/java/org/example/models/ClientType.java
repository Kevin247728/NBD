package org.example.models;


import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "client_type")
public abstract class ClientType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int maxBooks;
    private int maxRentDays;

    public ClientType(int maxBooks, int maxRentDays) {}

    public ClientType() {}

    public int getMaxBooks() {
        return maxBooks;
    }

    public int getMaxRentDays() {
        return maxRentDays;
    }
}

