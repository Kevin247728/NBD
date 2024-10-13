package org.example.repositories;

import org.example.exceptions.TooManyException;
import org.example.models.Rent;

import java.time.LocalDate;
import java.util.List;

public interface IRentRepository {
    Rent findById(Long id);
    List<Rent> findAll();
    void save(Rent rent);
    void delete(Rent rent);
    List<Rent> findByClientId(Long clientId);
    Rent rentBook(Long clientId, Long bookId, LocalDate beginDate, LocalDate endDate) throws TooManyException;
    int getCurrentRentCount(Long clientId);
}