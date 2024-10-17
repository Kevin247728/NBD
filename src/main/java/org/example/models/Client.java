package org.example.models;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import org.example.exceptions.TooManyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Client extends AbstractEntity {

    private String firstName;
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "client_type_id", nullable = false)
    private ClientType clientType;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rent> rents = new ArrayList<>();

    public Client(String firstName, String lastName, ClientType clientType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientType = clientType;
    }

    public Client() {}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(getEntityId(), client.getEntityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntityId());
    }
}
