package org.example.models;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.entity.naming.GetterStyle;
import java.time.LocalDate;
import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_book")
@CqlName("rents_by_client")
@PropertyStrategy(mutable = false, getterStyle = GetterStyle.JAVABEANS)
public class RentByClient extends Rent {

    @PartitionKey
    @CqlName("client_id")
    private UUID clientId;

    @ClusteringColumn(0)
    @CqlName("begin_date")
    private LocalDate beginDate;

    @ClusteringColumn(1)
    @CqlName("rent_id")
    private UUID rentId;

    @CqlName("book_id")
    private UUID bookId;

    private float fee;

    public RentByClient(UUID clientId, LocalDate beginDate, UUID rentId, float fee, LocalDate endDate, UUID bookId) {
        super(rentId, beginDate, endDate, fee, clientId, bookId);
    }
}
