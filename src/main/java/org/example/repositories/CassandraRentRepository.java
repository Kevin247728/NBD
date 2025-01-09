package org.example.repositories;

import com.datastax.oss.driver.api.core.CqlSession;
import org.example.DAO.RentDao;
import org.example.mappers.RentMapper;
import org.example.mappers.RentMapperBuilder;
import org.example.models.Rent;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CassandraRentRepository implements RentRepository {

    private final RentDao rentDao;

    public CassandraRentRepository(CqlSession session) {
        RentMapper rentMapper = new RentMapperBuilder(session).build();
        this.rentDao = rentMapper.rentDao();
    }

    public Rent create(UUID rentId, UUID clientId, float fee, LocalDate beginDate, LocalDate endDate, UUID bookId) {
        Rent rent = new Rent(rentId, beginDate, endDate, fee, clientId, bookId);
        return rentDao.create(rent);
    }

    public Rent findById(UUID rentId, UUID bookId, LocalDate beginDate) {
        return rentDao.findById(rentId, bookId, beginDate);
    }

    public Rent delete(UUID rentId, UUID bookId, LocalDate beginDate) {
        Rent rent = rentDao.findById(rentId, bookId, beginDate);
        rentDao.delete(rent);
        return rent;
    }

    public List<Rent> findByClientId(UUID clientId) {
        return rentDao.getAllRentsForClient(clientId);
    }

    public List<Rent> findAll() {
        return rentDao.getAllRents();
    }

    public void update(Rent rent) {
        rentDao.update(rent);
    }

    public long countRentsByClientId(UUID clientId) {
        return rentDao.countRentsByClientId(clientId);
    }
}
