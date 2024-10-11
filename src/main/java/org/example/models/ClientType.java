package org.example.models;


import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "client_type")
@Access(AccessType.FIELD)
public abstract class ClientType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

