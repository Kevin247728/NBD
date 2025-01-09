package org.example.repositories;
import org.example.models.Book;
import java.util.List;
import java.util.UUID;

public interface BookRepository {
    Book findById(UUID id);
    List<Book> findAll();
    Book create(UUID id, String title, boolean isRented);
    Book delete(UUID id);
    void update(Book book);
}
