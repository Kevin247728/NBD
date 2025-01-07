package org.example.DAO;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import org.example.models.Book;
import org.example.providers.BookQueryProvider;

import java.util.List;
import java.util.UUID;

@Dao
public interface BookDao {

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @QueryProvider(providerClass = BookQueryProvider.class, entityHelpers = {Book.class})
    Book findById(UUID id);

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = BookQueryProvider.class, entityHelpers = {Book.class})
    void create(Book book);

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = BookQueryProvider.class, entityHelpers = {Book.class})
    void delete(Book book);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @QueryProvider(providerClass = BookQueryProvider.class, entityHelpers = {Book.class})
    List<Book> getAllBooks();

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = BookQueryProvider.class, entityHelpers = {Book.class})
    void update(Book book);
}

