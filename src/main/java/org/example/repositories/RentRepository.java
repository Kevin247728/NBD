package org.example.repositories;

import org.example.exceptions.BookAlreadyRentedException;
import org.example.exceptions.TooManyException;
import org.example.models.Rent;
import java.util.List;
import java.util.UUID;

public interface RentRepository {
    Rent findById(UUID id);
    List<Rent> findAll();
    boolean delete(Rent rent);
    List<Rent> findByClientId(UUID clientId);
    void create(Rent rent) throws TooManyException, BookAlreadyRentedException;
    boolean update(Rent rent);
    int getCurrentRentCount(UUID clientId);
}