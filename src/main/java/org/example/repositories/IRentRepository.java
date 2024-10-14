package org.example.repositories;

import org.example.exceptions.BookAlreadyRentedException;
import org.example.exceptions.TooManyException;
import org.example.models.Rent;

import java.time.LocalDate;
import java.util.List;

public interface IRentRepository {
    Rent findById(Long id);
    List<Rent> findAll();
    void delete(Rent rent);
    List<Rent> findByClientId(Long clientId);
    Rent save(Long clientId, Long bookId, LocalDate beginDate, LocalDate endDate) throws TooManyException, BookAlreadyRentedException;
    int getCurrentRentCount(Long clientId);
    boolean isBookCurrentlyRented(Long bookId);
}