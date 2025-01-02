//package org.example.repositories;
//
//import com.mongodb.client.ClientSession;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.model.ReplaceOptions;
//import com.mongodb.client.model.Updates;
//import com.mongodb.client.result.UpdateResult;
//import org.bson.conversions.Bson;
//import org.example.exceptions.BookAlreadyRentedException;
//import org.example.exceptions.TooManyException;
//import org.example.models.Book;
//import org.example.models.Client;
//import org.example.models.Rent;
//import org.example.models.UniqueIdMgd;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MgdRentRepository extends AbstractMongoRepository implements RentRepository {
//
//    private final MongoCollection<Rent> rentCollection;
//    private final MongoCollection<Client> clientCollection;
//    private final MongoCollection<Book> bookCollection;
//    private final RedisMongoBookRepositoryDecorator bookRepositoryDecorator;
//
//    public MgdRentRepository(RedisMongoBookRepositoryDecorator bookRepositoryDecorator) {
//        this.bookCollection = mongoDatabase.getCollection("books", Book.class);
//        this.clientCollection = mongoDatabase.getCollection("clients", Client.class);
//        this.rentCollection = mongoDatabase.getCollection("rents", Rent.class);
//        this.bookRepositoryDecorator = bookRepositoryDecorator;
//    }
//
//    @Override
//    public void create(Rent rent) throws TooManyException, BookAlreadyRentedException {
//        Client client = clientCollection.find(Filters.eq("_id", rent.getClientId())).first();
//        if (client == null) {
//            throw new IllegalArgumentException("Client not found");
//        }
//
//        int currentRentCount = getCurrentRentCount(client.getEntityId());
//        if (currentRentCount >= client.getClientType().getMaxBooks()) {
//            throw new TooManyException("Client has already rented the maximum number of books.");
//        }
//
//        try (ClientSession clientSession = mongoClient.startSession()) {
//            clientSession.startTransaction();
//
//            Bson filterBook = Filters.eq("_id", rent.getBookId());
//            Bson updateBook = Updates.set("isRented", true);
//
//            UpdateResult updateResult = bookCollection.updateOne(clientSession, filterBook, updateBook);
//            if (updateResult.getModifiedCount() == 0) {
//                clientSession.abortTransaction();
//                throw new BookAlreadyRentedException("The book is already rented.");
//            }
//
//            Book book = bookCollection.find(filterBook).first();
//            if (book != null) {
//                book.setRented(true);
//                bookRepositoryDecorator.getRedisRepository().update(book);
//                Bson filter = Filters.eq("_id", book.getEntityId());
//                ReplaceOptions options = new ReplaceOptions().upsert(false);
//                bookCollection.replaceOne(clientSession, filter, book, options);
//            }
//
//            rent.calculateFee(client);
//            client.addRent(rent);
//
//            rentCollection.insertOne(clientSession, rent);
//
//            clientSession.commitTransaction();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to create rent: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public Rent findById(UniqueIdMgd id) {
//        Rent rent = rentCollection.find(Filters.eq("_id", id)).first();
////        if (rent == null) {
////            throw new EntityNotFoundException("Rent with ID " + id + " not found");
////        }
//        return rent;
//    }
//
//    @Override
//    public List<Rent> findAll() {
//        List<Rent> rents = new ArrayList<>();
//        rentCollection.find().into(rents);
//        return rents;
//    }
//
//    @Override
//    public boolean delete(Rent rent) {
//        try (ClientSession clientSession = mongoClient.startSession()) {
//            clientSession.startTransaction();
//            Client client = clientCollection.find(Filters.eq("_id", rent.getClientId())).first();
//            Book book = bookCollection.find(Filters.eq("_id", rent.getBookId())).first();
//            rent.returnBook(client);
//            if (book != null) {
//                book.setRented(false);
//                bookRepositoryDecorator.getRedisRepository().update(book);
//                Bson filter = Filters.eq("_id", book.getEntityId());
//                ReplaceOptions options = new ReplaceOptions().upsert(false);
//                bookCollection.replaceOne(clientSession, filter, book, options);
//            }
//            boolean deleteSucceeded =  rentCollection.deleteOne(clientSession, Filters.eq("_id", rent.getEntityId())).getDeletedCount() > 0;
//            clientSession.commitTransaction();
//            return deleteSucceeded;
//
//        } catch (RuntimeException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public boolean update(Rent rent) {
//        try (ClientSession clientSession = mongoClient.startSession()) {
//            clientSession.startTransaction();
//            Bson filter = Filters.eq("_id", rent.getEntityId());
//            ReplaceOptions options = new ReplaceOptions().upsert(false);
//            boolean updateSucceeded =  rentCollection.replaceOne(clientSession, filter, rent, options).getModifiedCount() > 0;
//            clientSession.commitTransaction();
//            return updateSucceeded;
//        } catch (RuntimeException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public List<Rent> findByClientId(UniqueIdMgd clientId) {
//        List<Rent> rents = new ArrayList<>();
//        rentCollection.find(Filters.eq("clientId", clientId)).into(rents);
//        return rents;
//    }
//
//    @Override
//    public int getCurrentRentCount(UniqueIdMgd clientId) {
//        long count = rentCollection.countDocuments(Filters.eq("clientId", clientId));
//        return (int) count;
//    }
//}
