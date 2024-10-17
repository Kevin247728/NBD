package org.example.repositories;

import org.example.exceptions.BookAlreadyRentedException;
import org.example.exceptions.TooManyException;
import org.example.models.Rent;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IRentRepository {
    Rent findById(UUID id);
    List<Rent> findAll();
    void delete(Rent rent);
    List<Rent> findByClientId(UUID clientId);
    void save(Rent rent) throws TooManyException, BookAlreadyRentedException;
    int getCurrentRentCount(UUID clientId);
    boolean isBookCurrentlyRented(UUID bookId);
}