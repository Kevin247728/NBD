package org.example.models;


import com.sun.istack.NotNull;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "client_type")
public abstract class ClientType extends AbstractEntity {

    @NotNull
    private int maxBooks;

    @NotNull
    private int maxRentDays;

    public ClientType(int maxBooks, int maxRentDays) {
        this.maxBooks = maxBooks;
        this.maxRentDays = maxRentDays;
    }

    public ClientType() {}

    public int getMaxBooks() {
        return maxBooks;
    }

    public int getMaxRentDays() {
        return maxRentDays;
    }
}

