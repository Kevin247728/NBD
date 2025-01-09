package org.example.models;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.entity.naming.GetterStyle;
import java.time.LocalDate;
import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_book")
@CqlName("rents_by_book")
@PropertyStrategy(mutable = false, getterStyle = GetterStyle.JAVABEANS)
public class RentByBook extends Rent {

    @PartitionKey
    @CqlName("book_id")
    private UUID bookId;

    @ClusteringColumn(0)
    @CqlName("begin_date")
    private LocalDate beginDate;

    @ClusteringColumn(1)
    @CqlName("rent_id")
    private UUID rentId;

    @CqlName("client_id")
    private UUID clientId;

    private float fee;

    public RentByBook(UUID bookId, LocalDate beginDate, UUID rentId, float fee, LocalDate endDate, UUID clientId) {
        super(rentId, beginDate, endDate, fee, clientId, bookId);
    }
}

