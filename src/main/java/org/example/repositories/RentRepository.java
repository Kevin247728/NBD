package org.example.repositories;

import org.example.models.Rent;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RentRepository {
    Rent findById(UUID rentId, UUID bookId, LocalDate beginDate);
    List<Rent> findAll();
    Rent delete(UUID rentId, UUID bookId, LocalDate beginDate);
    List<Rent> findByClientId(UUID clientId);
    Rent create(UUID rentId, UUID clientId, float fee, LocalDate beginDate, LocalDate endDate, UUID bookId);
    void update(Rent rent);
    long countRentsByClientId(UUID clientId);
}