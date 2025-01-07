package org.example.managers;

import org.example.models.Book;
import org.example.repositories.CassandraBookRepository;

import java.util.List;
import java.util.UUID;

public class BookManager {

    private final CassandraBookRepository bookRepository;

    public BookManager(CassandraBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void createBook(String title, boolean isRented) {
        UUID id = UUID.randomUUID();
        bookRepository.create(id, title, isRented);
    }

    public Book getBookById(UUID id) {
        return bookRepository.findById(id);
    }

    public Book removeBook(UUID id) {
        return bookRepository.delete(id);
    }

    public List<Book> listAllBooks() {
        return bookRepository.findAll();
    }

    public void updateBookInfo(Book book) {
        bookRepository.update(book);
    }
}
