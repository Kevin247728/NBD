package org.example.repositories;

import org.example.models.Book;

import java.util.List;

public interface IBookRepository {
    Book findById(Long id);
    List<Book> findAll();
    void save(Book book);
    void delete(Book book);
}
