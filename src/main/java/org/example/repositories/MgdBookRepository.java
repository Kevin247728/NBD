//package org.example.repositories;
//
//import com.mongodb.client.ClientSession;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.model.*;
//import org.bson.Document;
//import org.example.exceptions.EntityNotFoundException;
//import org.example.models.Book;
//import org.example.models.UniqueIdMgd;
//import org.bson.conversions.Bson;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MgdBookRepository extends AbstractMongoRepository implements BookRepository {
//
//    private final MongoCollection<Book> bookCollection;
//
//    public MgdBookRepository() {
//        createBooksCollection();
//        this.bookCollection = mongoDatabase.getCollection("books", Book.class);
//    }
//
//    private void createBooksCollection() {
//        ValidationOptions validationOptions = new ValidationOptions().validator(Document.parse("""
//            {
//                $jsonSchema: {
//                    "bsonType": "object",
//                    "required": ["title", "isRented"],
//                    "properties": {
//                        "title": {
//                            "bsonType": "string",
//                            "minLength": 1,
//                            "description": "Title is required and must be a non-empty string"
//                        },
//                        "isRented": {
//                            "bsonType": "bool",
//                            "description": "isRented must be true for rented and false for available"
//                        }
//                    }
//                }
//            }
//        """)).validationAction(ValidationAction.ERROR);
//
//        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(validationOptions);
//
//        if (!collectionExists()) {
//            mongoDatabase.createCollection("books", createCollectionOptions);
//        }
//    }
//
//    private boolean collectionExists() {
//        for (String name : mongoDatabase.listCollectionNames()) {
//            if (name.equalsIgnoreCase("books")) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void create(Book book) {
//        try (ClientSession clientSession = mongoClient.startSession()) {
//            clientSession.startTransaction();
//            bookCollection.insertOne(clientSession, book);
//            clientSession.commitTransaction();
//        } catch (RuntimeException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public Book findById(UniqueIdMgd id) {
//        return bookCollection.find(Filters.eq("_id", id)).first();
//    }
//
//    @Override
//    public List<Book> findAll() {
//        List<Book> books = new ArrayList<>();
//        bookCollection.find().into(books);
//        return books;
//    }
//
//    @Override
//    public boolean update(Book book) {
//        try (ClientSession clientSession = mongoClient.startSession()) {
//            clientSession.startTransaction();
//            Bson filter = Filters.eq("_id", book.getEntityId());
//            ReplaceOptions options = new ReplaceOptions().upsert(false);
//            boolean updateSucceeded = bookCollection.replaceOne(clientSession, filter, book, options).getModifiedCount() > 0;
//            clientSession.commitTransaction();
//            return updateSucceeded;
//        } catch (RuntimeException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public boolean delete(Book book) {
//        try (ClientSession clientSession = mongoClient.startSession()) {
//            clientSession.startTransaction();
//            boolean deleteSucceeded = bookCollection.deleteOne(clientSession, Filters.eq("_id", book.getEntityId())).getDeletedCount() > 0;
//            clientSession.commitTransaction();
//            return deleteSucceeded;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
