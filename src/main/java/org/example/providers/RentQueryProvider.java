package org.example.providers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import org.example.constants.RentConsts;
import org.example.models.Rent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class RentQueryProvider {

    private final CqlSession session;

    public RentQueryProvider(MapperContext context) {
        this.session = context.getSession();
    }

    public Rent create(Rent rent) {
        Insert insertRentByClient = QueryBuilder.insertInto(RentConsts.RENTS_BY_CLIENT)
                .value(RentConsts.CLIENT_ID, QueryBuilder.literal(rent.getClientId()))
                .value(RentConsts.RENT_ID, QueryBuilder.literal(rent.getRentId()))
                .value(RentConsts.BEGIN_DATE, QueryBuilder.literal(rent.getBeginDate()))
                .value(RentConsts.END_DATE, QueryBuilder.literal(rent.getEndDate()))
                .value(RentConsts.FEE, QueryBuilder.literal(rent.getFee()))
                .value(RentConsts.BOOK_ID, QueryBuilder.literal(rent.getBookId()));

        Insert insertRentByBook = QueryBuilder.insertInto(RentConsts.RENTS_BY_BOOK)
                .value(RentConsts.BOOK_ID, QueryBuilder.literal(rent.getBookId()))
                .value(RentConsts.RENT_ID, QueryBuilder.literal(rent.getRentId()))
                .value(RentConsts.CLIENT_ID, QueryBuilder.literal(rent.getClientId()))
                .value(RentConsts.BEGIN_DATE, QueryBuilder.literal(rent.getBeginDate()))
                .value(RentConsts.END_DATE, QueryBuilder.literal(rent.getEndDate()))
                .value(RentConsts.FEE, QueryBuilder.literal(rent.getFee()));

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(insertRentByClient.build())
                .addStatement(insertRentByBook.build())
                .build();

        session.execute(batchStatement);
        return rent;
    }

    public void delete(Rent rent) {
        Delete deleteRentByClient = QueryBuilder.deleteFrom(RentConsts.RENTS_BY_CLIENT)
                .where(Relation.column(RentConsts.CLIENT_ID).isEqualTo(QueryBuilder.literal(rent.getClientId())))
                .where(Relation.column(RentConsts.RENT_ID).isEqualTo(QueryBuilder.literal(rent.getRentId())))
                .where(Relation.column(RentConsts.BEGIN_DATE).isEqualTo(QueryBuilder.literal(rent.getBeginDate())));

        Delete deleteRentByBook = QueryBuilder.deleteFrom(RentConsts.RENTS_BY_BOOK)
                .where(Relation.column(RentConsts.BOOK_ID).isEqualTo(QueryBuilder.literal(rent.getBookId())))
                .where(Relation.column(RentConsts.RENT_ID).isEqualTo(QueryBuilder.literal(rent.getRentId())))
                .where(Relation.column(RentConsts.BEGIN_DATE).isEqualTo(QueryBuilder.literal(rent.getBeginDate())));

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(deleteRentByClient.build())
                .addStatement(deleteRentByBook.build())
                .build();

        session.execute(batchStatement);
    }

    public Rent findById(UUID rentId, UUID bookId, LocalDate beginDate) {
        Select selectRent = QueryBuilder
                .selectFrom(RentConsts.RENTS_BY_BOOK)
                .all()
                .where(Relation.column(RentConsts.BOOK_ID).isEqualTo(literal(bookId)))
                .where(Relation.column(RentConsts.BEGIN_DATE).isEqualTo(literal(beginDate)))
                .where(Relation.column(RentConsts.RENT_ID).isEqualTo(literal(rentId)));
        Row row = session.execute(selectRent.build()).one();

        return getRent(row);
    }

    public List<Rent> getAllRentsForClient(UUID clientId) {
        Select allRents = QueryBuilder
                .selectFrom(RentConsts.RENTS_BY_CLIENT)
                .all()
                .where(Relation.column(RentConsts.CLIENT_ID).isEqualTo(literal(clientId)));
        List<Row> rows = session.execute(allRents.build()).all();
        List<Rent> rents = new ArrayList<>();

        for (Row row : rows) {
            rents.add(getRent(row));
        }

        return rents;
    }

    public void update(Rent rent) {
        Update updateRentByClient = QueryBuilder.update(RentConsts.RENTS_BY_CLIENT)
                .setColumn(RentConsts.END_DATE, QueryBuilder.literal(rent.getEndDate()))
                .setColumn(RentConsts.FEE, QueryBuilder.literal(rent.getFee()))
                .setColumn(RentConsts.BOOK_ID, QueryBuilder.literal(rent.getBookId()))
                .where(Relation.column(RentConsts.CLIENT_ID).isEqualTo(QueryBuilder.literal(rent.getClientId())))
                .where(Relation.column(RentConsts.BEGIN_DATE).isEqualTo(QueryBuilder.literal(rent.getBeginDate())))
                .where(Relation.column(RentConsts.RENT_ID).isEqualTo(QueryBuilder.literal(rent.getRentId())));

        Update updateRentByBook = QueryBuilder.update(RentConsts.RENTS_BY_BOOK)
                .setColumn(RentConsts.END_DATE, QueryBuilder.literal(rent.getEndDate()))
                .setColumn(RentConsts.FEE, QueryBuilder.literal(rent.getFee()))
                .setColumn(RentConsts.CLIENT_ID, QueryBuilder.literal(rent.getClientId()))
                .where(Relation.column(RentConsts.BOOK_ID).isEqualTo(QueryBuilder.literal(rent.getBookId())))
                .where(Relation.column(RentConsts.BEGIN_DATE).isEqualTo(QueryBuilder.literal(rent.getBeginDate())))
                .where(Relation.column(RentConsts.RENT_ID).isEqualTo(QueryBuilder.literal(rent.getRentId())));

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(updateRentByClient.build())
                .addStatement(updateRentByBook.build())
                .build();

        session.execute(batchStatement);
    }

    public List<Rent> getAllRents() {
        Select allRents = QueryBuilder.selectFrom(RentConsts.RENTS_BY_BOOK).all();
        List<Row> rows = session.execute(allRents.build()).all();

        List<Rent> rents = new ArrayList<>();
        for (Row row : rows) {
            rents.add(getRent(row));
        }

        return rents;
    }

    public long countRentsByClientId(UUID clientId) {
        SimpleStatement countQuery = SimpleStatement.builder(
                "SELECT COUNT(rent_id) FROM rent_a_book.rents_by_client WHERE client_id = ?")
                .addPositionalValue(clientId)
                .build();

        Row row = session.execute(countQuery).one();

        // Jeśli wynik nie jest nullem, zwracamy liczbę wypożyczeń, w przeciwnym razie 0
        return row != null ? row.getLong(0) : 0;
    }



    private Rent getRent(Row rent) {
        return new Rent(
                rent.getUuid(RentConsts.RENT_ID),
                rent.getLocalDate(RentConsts.BEGIN_DATE),
                rent.getLocalDate(RentConsts.END_DATE),
                rent.getFloat(RentConsts.FEE),
                rent.getUuid(RentConsts.CLIENT_ID),
                rent.getUuid(RentConsts.BOOK_ID)
        );
    }
}
