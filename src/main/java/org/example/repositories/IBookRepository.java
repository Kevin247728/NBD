package org.example.repositories;

import org.example.models.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IBookRepository {
    Book findById(UUID id);
    List<Book> findAll();
    void save(Book book);
    void delete(Book book);
}
