package org.example.repositories;

import com.mongodb.ConnectionString;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.example.models.Book;
import org.example.models.UniqueIdMgd;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class MgdBookRepository extends AbstractMongoRepository implements BookRepository {

    private final MongoCollection<Book> bookCollection;

    public MgdBookRepository() {
        this.bookCollection = mongoDatabase.getCollection("books", Book.class);
    }

    @Override
    public void create(Book book) {
        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            bookCollection.insertOne(clientSession, book);
            clientSession.commitTransaction();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book findById(UniqueIdMgd id) {
        return bookCollection.find(Filters.eq("_id", id)).first();
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        bookCollection.find().into(books);
        return books;
    }

    @Override
    public boolean update(Book book) {
        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            Bson filter = Filters.eq("_id", book.getEntityId());
            ReplaceOptions options = new ReplaceOptions().upsert(false);
            boolean updateSucceeded = bookCollection.replaceOne(clientSession, filter, book, options).getModifiedCount() > 0;
            clientSession.commitTransaction();
            return updateSucceeded;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Book book) {
        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            boolean deleteSucceeded = bookCollection.deleteOne(clientSession, Filters.eq("_id", book.getEntityId())).getDeletedCount() > 0;
            clientSession.commitTransaction();
            return deleteSucceeded;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
