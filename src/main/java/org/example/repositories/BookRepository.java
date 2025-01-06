package org.example.repositories;
import org.example.models.Book;
import java.util.List;
import java.util.UUID;

public interface BookRepository {
    Book findById(UUID id);
    List<Book> findAll();
    void create(Book book);
    boolean delete(Book book);
    boolean update(Book book);
}
