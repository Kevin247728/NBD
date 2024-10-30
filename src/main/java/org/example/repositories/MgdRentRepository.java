package org.example.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.conversions.Bson;
import org.example.exceptions.BookAlreadyRentedException;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.TooManyException;
import org.example.models.Book;
import org.example.models.Client;
import org.example.models.Rent;
import org.example.models.UniqueIdMgd;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MgdRentRepository implements RentRepository {

    private final MongoCollection<Rent> rentCollection;
    private final MongoCollection<Client> clientCollection;
    private final MongoCollection<Book> bookCollection;

    public MgdRentRepository(MongoDatabase database) {
        this.rentCollection = database.getCollection("rents", Rent.class);
        this.clientCollection = database.getCollection("clients", Client.class);
        this.bookCollection = database.getCollection("books", Book.class);
    }

    @Override
    public void create(Rent rent) throws TooManyException, BookAlreadyRentedException {
        Client client = clientCollection.find(Filters.eq("_id", rent.getClientId())).first();
        if (client == null) {
            throw new IllegalArgumentException("Client not found");
        }

        int currentRentCount = getCurrentRentCount(client.getEntityId());
        if (currentRentCount >= client.getClientType().getMaxBooks()) {
            throw new TooManyException("Client has already rented the maximum number of books.");
        }

        Book book = bookCollection.find(Filters.eq("_id", rent.getBookId())).first();
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }

        if (isBookCurrentlyRented(book.getEntityId())) {
            throw new BookAlreadyRentedException("The book is already rented.");
        }

        if (rent.getEntityId() == null) {
            rentCollection.insertOne(rent);
        } else {
            Bson filter = Filters.eq("_id", rent.getEntityId());
            ReplaceOptions options = new ReplaceOptions().upsert(true);
            rentCollection.replaceOne(filter, rent, options);
        }
    }

    @Override
    public Rent findById(UniqueIdMgd id) {
        Rent rent = rentCollection.find(Filters.eq("_id", id)).first();
        if (rent == null) {
            throw new EntityNotFoundException("Rent with ID " + id + " not found");
        }
        return rent;
    }

    @Override
    public List<Rent> findAll() {
        List<Rent> rents = new ArrayList<>();
        rentCollection.find().into(rents);
        return rents;
    }

    @Override
    public boolean delete(Rent rent) {
        return rentCollection.deleteOne(Filters.eq("_id", rent.getEntityId())).getDeletedCount() > 0;
    }

    @Override
    public boolean update(Rent rent) {
        Bson filter = Filters.eq("_id", rent.getEntityId());
        ReplaceOptions options = new ReplaceOptions().upsert(false);
        return rentCollection.replaceOne(filter, rent, options).getModifiedCount() > 0;
    }

    @Override
    public List<Rent> findByClientId(UniqueIdMgd clientId) {
        List<Rent> rents = new ArrayList<>();
        rentCollection.find(Filters.eq("clientId", clientId)).into(rents);
        return rents;
    }

    @Override
    public int getCurrentRentCount(UniqueIdMgd clientId) {
        long count = rentCollection.countDocuments(Filters.eq("clientId", clientId));
        return (int) count;
    }

    @Override
    public boolean isBookCurrentlyRented(UniqueIdMgd bookId) {
        long count = rentCollection.countDocuments(
                Filters.and(Filters.eq("bookId", bookId), Filters.gt("endDate", LocalDate.now()))
        );
        return count > 0;
    }
}
