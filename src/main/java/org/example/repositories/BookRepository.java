package org.example.repositories;

import org.example.models.Book;

import java.util.List;
import java.util.UUID;

public interface BookRepository {
    Book findById(UUID id);
    List<Book> findAll();
    void save(Book book);
    void delete(Book book);
}
