package org.example.repositories;
import org.example.models.Book;
import org.example.models.UniqueIdMgd;

import java.util.List;

public interface BookRepository {
    Book findById(UniqueIdMgd id);
    List<Book> findAll();
    void create(Book book);
    boolean delete(Book book);
    boolean update(Book book);
}
