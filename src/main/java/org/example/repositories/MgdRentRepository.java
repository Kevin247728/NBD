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

import java.io.Console;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MgdRentRepository extends AbstractMongoRepository implements RentRepository {

    private final MongoCollection<Rent> rentCollection;
    private final MongoCollection<Client> clientCollection;
    private final MongoCollection<Book> bookCollection;

    public MgdRentRepository() {
        this.bookCollection = mongoDatabase.getCollection("books", Book.class);
        this.clientCollection = mongoDatabase.getCollection("clients", Client.class);
        this.rentCollection = mongoDatabase.getCollection("rents", Rent.class);
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

        if (book.isRented()) {
            throw new BookAlreadyRentedException("The book is already rented.");
        }

        rent.calculateFee(client);
        client.addRent(rent);
        book.setRented(true);

        // need to update the Book in the database now to include the rented true
        Bson filterBook = Filters.eq("_id", book.getEntityId());
        ReplaceOptions optionsBook = new ReplaceOptions().upsert(false);
        bookCollection.replaceOne(filterBook, book, optionsBook);

        // need to update the Client in the database now to include the new rent
        Bson filterClient = Filters.eq("_id", client.getEntityId());
        ReplaceOptions optionsClient = new ReplaceOptions().upsert(false);
        clientCollection.replaceOne(filterClient, client, optionsClient);

        if (findById(rent.getEntityId()) == null) {
            rentCollection.insertOne(rent);
        }
        else {
            Bson filter = Filters.eq("_id", rent.getEntityId());
            ReplaceOptions options = new ReplaceOptions().upsert(true);
            rentCollection.replaceOne(filter, rent, options);
        }
    }

    @Override
    public Rent findById(UniqueIdMgd id) {
        Rent rent = rentCollection.find(Filters.eq("_id", id)).first();
//        if (rent == null) {
//            throw new EntityNotFoundException("Rent with ID " + id + " not found");
//        }
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
        Client client = clientCollection.find(Filters.eq("_id", rent.getClientId())).first();
        Book book = bookCollection.find(Filters.eq("_id", rent.getBookId())).first();
        rent.returnBook(client);
        book.setRented(false);
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
}
