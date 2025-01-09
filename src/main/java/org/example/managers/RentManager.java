package org.example.managers;

import org.example.exceptions.BookAlreadyRentedException;
import org.example.exceptions.RentDurationExceeded;
import org.example.exceptions.TooManyException;
import org.example.models.Book;
import org.example.models.Client;
import org.example.models.Rent;
import org.example.repositories.CassandraBookRepository;
import org.example.repositories.CassandraClientRepository;
import org.example.repositories.CassandraRentRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentManager {

    private final CassandraRentRepository rentRepository;
    private final CassandraBookRepository bookRepository;
    private final CassandraClientRepository clientRepository;

    public RentManager(CassandraRentRepository rentRepository, CassandraBookRepository bookRepository, CassandraClientRepository clientRepository) {
        this.rentRepository = rentRepository;
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
    }

    public Rent createRent(UUID clientId, UUID bookId, LocalDate beginDate, LocalDate endDate, float fee) throws BookAlreadyRentedException, TooManyException {

        Optional<Client> clientOptional = clientRepository.findById(clientId);
        if (clientOptional.isEmpty()) {
            throw new IllegalArgumentException("Client does not exist!");
        }

        Client client = clientOptional.get();
        int maxRentDays = client.getMaxRentDays();
        long proposedRentDays = java.time.temporal.ChronoUnit.DAYS.between(beginDate, endDate);
        if (proposedRentDays > maxRentDays) {
            throw new RentDurationExceeded("Proposed rent duration exceeds the maximum allowed days for the client!");
        }

        long currentRents = rentRepository.countRentsByClientId(clientId);
        int maxRents = client.getMaxBooks();
        if (currentRents == maxRents) {
            throw new TooManyException("Client has reached the maximum number of rents!");
        }

        Book book = bookRepository.findById(bookId);
        if (book.isRented()) {
            throw new BookAlreadyRentedException("Book is already rented!");
        }

        UUID rentId = UUID.randomUUID();
        rentRepository.create(rentId, clientId, fee, beginDate, endDate, bookId);
        Rent rent = rentRepository.findById(rentId, book.getId(), beginDate);
        rent.calculateFee(client);
        rentRepository.update(rent);

        book.setRented(true);
        bookRepository.update(book);
        return rent;
    }

    public Rent getRentById(UUID rentId, UUID bookId, LocalDate beginDate) {
        return rentRepository.findById(rentId, bookId, beginDate);
    }

    public Rent removeRent(UUID rentId, UUID bookId, LocalDate beginDate) {
        Rent rent = rentRepository.findById(rentId, bookId, beginDate);

        if (rent == null) {
            throw new IllegalArgumentException("Rent not found!");
        }

        rentRepository.delete(rentId, bookId, beginDate);

        Book book = bookRepository.findById(rent.getBookId());
        if (book != null) {
            book.setRented(false);
            bookRepository.update(book);
        }

        return rent;
    }

    public List<Rent> listRentsByClientId(UUID clientId) {
        return rentRepository.findByClientId(clientId);
    }

    public List<Rent> listAllRents() {
        return rentRepository.findAll();
    }

    public void updateRentInfo(Rent rent) {
        rentRepository.update(rent);
    }

    public long countRentsForClient(UUID clientId) {
        return rentRepository.countRentsByClientId(clientId);
    }
}
