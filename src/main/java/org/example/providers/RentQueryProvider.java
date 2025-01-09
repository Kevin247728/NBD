package org.example.providers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import org.example.constants.RentConsts;
import org.example.models.Rent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class RentQueryProvider {

    private final CqlSession session;
    private final EntityHelper<Rent> rentHelper;

    public RentQueryProvider(MapperContext context, EntityHelper<Rent> rentHelper) {
        this.session = context.getSession();
        this.rentHelper = rentHelper;
    }

    public Rent create(Rent rent) {
        session.execute(
                session.prepare(rentHelper.insert().build()).bind()
                        .setUuid(RentConsts.RENT_ID, rent.getRentId())
                        .setLocalDate(RentConsts.BEGIN_DATE, rent.getBeginDate())
                        .setLocalDate(RentConsts.END_DATE, rent.getEndDate())
                        .setFloat(RentConsts.FEE, rent.getFee())
                        .setUuid(RentConsts.CLIENT_ID, rent.getClientId())
                        .setUuid(RentConsts.BOOK_ID, rent.getBookId())
        );
        return rent;
    }

    public void delete(Rent rent) {
        session.execute(
                session.prepare(rentHelper.deleteByPrimaryKey().build()).bind()
                        .setUuid(RentConsts.RENT_ID, rent.getRentId())
                        .setLocalDate(RentConsts.BEGIN_DATE, rent.getBeginDate())
                        .setLocalDate(RentConsts.END_DATE, rent.getEndDate())
        );
    }

    public Rent findById(UUID rentId) {
        Select selectRent = QueryBuilder
                .selectFrom(RentConsts.RENTS)
                .all()
                .where(Relation.column(RentConsts.RENT_ID).isEqualTo(literal(rentId)));
        Row row = session.execute(selectRent.build()).one();

        return getRent(row);
    }

    public List<Rent> getAllRentsForClient(UUID clientId) {
        Select allRents = QueryBuilder
                .selectFrom(RentConsts.RENTS)
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
        session.execute(
                session.prepare(rentHelper.updateByPrimaryKey().build()).bind()
                        .setUuid(RentConsts.CLIENT_ID, rent.getClientId())
                        .setLocalDate(RentConsts.BEGIN_DATE, rent.getBeginDate())
                        .setUuid(RentConsts.RENT_ID, rent.getRentId())
                        .setFloat(RentConsts.FEE, rent.getFee())
                        .setLocalDate(RentConsts.END_DATE, rent.getEndDate())
                        .setUuid(RentConsts.BOOK_ID, rent.getBookId())
        );
    }

    public List<Rent> getAllRents() {
        Select allRents = QueryBuilder.selectFrom(RentConsts.RENTS).all();
        List<Row> rows = session.execute(allRents.build()).all();

        List<Rent> rents = new ArrayList<>();
        for (Row row : rows) {
            rents.add(getRent(row));
        }

        return rents;
    }

    public long countRentsByClientId(UUID clientId) {
        SimpleStatement countQuery = SimpleStatement.builder(
                "SELECT COUNT(rent_id) FROM rent_a_book.rents WHERE client_id = ?")
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
