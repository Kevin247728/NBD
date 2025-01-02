//package org.example.repositories;
//
//import org.example.exceptions.BookAlreadyRentedException;
//import org.example.exceptions.TooManyException;
//import org.example.models.Rent;
//import org.example.models.UniqueIdMgd;
//
//import java.util.List;
//
//public interface RentRepository {
//    Rent findById(UniqueIdMgd id);
//    List<Rent> findAll();
//    boolean delete(Rent rent);
//    List<Rent> findByClientId(UniqueIdMgd clientId);
//    void create(Rent rent) throws TooManyException, BookAlreadyRentedException;
//    boolean update(Rent rent);
//    int getCurrentRentCount(UniqueIdMgd clientId);
//}