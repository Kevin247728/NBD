package org.example.models;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.entity.naming.GetterStyle;
import org.example.exceptions.TooManyException;
import java.time.LocalDate;
import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_book")
@CqlName("rents")
@PropertyStrategy(mutable = false, getterStyle = GetterStyle.JAVABEANS)
public class Rent {

    @CqlName("begin_date")
    private LocalDate beginDate;

    @CqlName("end_date")
    private LocalDate endDate;

    @ClusteringColumn
    @CqlName("rent_id")
    private UUID rentId;

    @PartitionKey
    @CqlName("client_id")
    private UUID clientId;

    @CqlName("book_id")
    private UUID bookId;

    private float fee;

    public Rent(UUID clientId, UUID bookId, LocalDate beginDate, LocalDate endDate) throws TooManyException {
        this.clientId = clientId;
        this.bookId = bookId;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.rentId = UUID.randomUUID();
    }

    public void calculateFee(Client client) {
        if (client instanceof NonStudent) {
            this.fee = ((NonStudent) client).getAdditionalFee();
        } else {
            this.fee = 0;
        }
    }

    public float getFee() {
        return fee;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

