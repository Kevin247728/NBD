package org.example.providers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import org.example.constants.BookConsts;
import org.example.models.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class BookQueryProvider {

    private final CqlSession session;
    private final EntityHelper<Book> bookHelper;

    public BookQueryProvider(MapperContext context, EntityHelper<Book> bookHelper) {
        this.session = context.getSession();
        this.bookHelper = bookHelper;
    }

    public Book create(Book book) {
        session.execute(
                session.prepare(bookHelper.insert().build()).bind()
                        .setUuid(BookConsts.ID, book.getId())
                        .setString(BookConsts.TITLE, book.getTitle())
                        .setBoolean(BookConsts.IS_RENTED, book.isRented())
        );
        return book;
    }

    public void delete(Book book) {
        session.execute(
                session.prepare(bookHelper.deleteByPrimaryKey().build()).bind()
                        .setUuid(BookConsts.ID, book.getId())
        );
    }

    public Book findById(UUID id) {
        Select selectBook = QueryBuilder
                .selectFrom(BookConsts.BOOKS)
                .all()
                .where(Relation.column(BookConsts.ID).isEqualTo(literal(id)));
        Row row = session.execute(selectBook.build()).one();

        return getBook(row);
    }

    public List<Book> getAllBooks() {
        Select all = QueryBuilder.selectFrom(BookConsts.BOOKS).all();
        List<Row> rows = session.execute(all.build()).all();
        List<Book> books = new ArrayList<>();

        for (Row row : rows) {
            books.add(getBook(row));
        }

        return books;
    }

    public void update(Book book) {
        session.execute(
                session.prepare(bookHelper.updateByPrimaryKey().build()).bind()
                        .setUuid(BookConsts.ID, book.getId())
                        .setString(BookConsts.TITLE, book.getTitle())
                        .setBoolean(BookConsts.IS_RENTED, book.isRented())
        );
    }

    private Book getBook(Row book) {
        return new Book(
                book.getUuid(BookConsts.ID),
                book.getString(BookConsts.TITLE),
                book.getBoolean(BookConsts.IS_RENTED)
        );
    }
}
