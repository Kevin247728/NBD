package org.example.repositories;

import org.example.exceptions.BookAlreadyRentedException;
import org.example.exceptions.TooManyException;
import org.example.models.Rent;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RentRepository {
    Rent findById(UUID rentId);
    List<Rent> findAll();
    Rent delete(UUID rentId);
    List<Rent> findByClientId(UUID clientId);
    Rent create(UUID rentId, UUID clientId, float fee, LocalDate beginDate, LocalDate endDate, UUID bookId);
    void update(Rent rent);
    long countRentsByClientId(UUID clientId);
}