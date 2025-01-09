package org.example.repositories;

import com.datastax.oss.driver.api.core.CqlSession;
import org.example.DAO.BookDao;
import org.example.mappers.BookMapper;
import org.example.mappers.BookMapperBuilder;
import org.example.models.Book;

import java.util.List;
import java.util.UUID;

public class CassandraBookRepository implements BookRepository {

    private final BookDao bookDao;

    public CassandraBookRepository(CqlSession session) {
        BookMapper bookMapper = new BookMapperBuilder(session).build();
        this.bookDao = bookMapper.bookDao();
    }

    public Book create(UUID id, String title, boolean isRented) {
        Book book = new Book(id, title, isRented);
        return bookDao.create(book);
    }

    public Book findById(UUID id) {
        return bookDao.findById(id);
    }

    public Book delete(UUID id) {
        Book book = bookDao.findById(id);
        bookDao.delete(book);
        return book;
    }

    public List<Book> findAll() {
        return bookDao.getAllBooks();
    }

    public void update(Book book) {
        bookDao.update(book);
    }
}
