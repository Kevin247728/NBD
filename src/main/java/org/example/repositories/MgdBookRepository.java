package org.example.repositories;

import com.mongodb.ConnectionString;
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
        bookCollection.insertOne(book);
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
        Bson filter = Filters.eq("_id", book.getEntityId());
        ReplaceOptions options = new ReplaceOptions().upsert(false);
        return bookCollection.replaceOne(filter, book, options).getModifiedCount() > 0;
    }

    @Override
    public boolean delete(Book book) {
        return bookCollection.deleteOne(Filters.eq("_id", book.getEntityId())).getDeletedCount() > 0;
    }
}
