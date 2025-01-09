package org.example.DAO;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import org.example.models.Rent;
import org.example.providers.RentQueryProvider;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Dao
public interface RentDao {

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = RentQueryProvider.class)
    Rent findById(UUID rentId, UUID bookId, LocalDate beginDate);

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = RentQueryProvider.class)
    Rent create(Rent rent);

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = RentQueryProvider.class)
    void delete(Rent rent);

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = RentQueryProvider.class)
    List<Rent> getAllRentsForClient(UUID clientId);

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = RentQueryProvider.class)
    void update(Rent rent);

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = RentQueryProvider.class)
    List<Rent> getAllRents();

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = RentQueryProvider.class)
    long countRentsByClientId(UUID clientId);
}

