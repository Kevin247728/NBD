package org.example.repositories;

import com.datastax.oss.driver.api.core.CqlSession;
import org.example.DAO.ClientDao;
import org.example.mappers.ClientMapper;
import org.example.mappers.ClientMapperBuilder;
import org.example.models.Client;
import org.example.models.NonStudent;
import org.example.models.Student;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CassandraClientRepository implements ClientRepository {

    private final ClientDao clientDao;

    public CassandraClientRepository(CqlSession session) {
        ClientMapper clientMapper = new ClientMapperBuilder(session).build();
        this.clientDao = clientMapper.clientDao();
    }

    public Client createStudent(UUID id, String firstName, String lastName, String discriminator, int maxBooks, int maxRentDays) {
        Client client = new Student(id, firstName, lastName, discriminator, maxBooks, maxRentDays);
        return clientDao.create(client);
    }

    public Client createNonStudent(UUID id, float additionalFee, String firstName, String lastName, String discriminator, int maxBooks, int maxRentDays) {
        Client client = new NonStudent(id, discriminator, additionalFee, firstName, lastName, maxBooks, maxRentDays);
        return clientDao.create(client);
    }

    public Optional<Client> findById(UUID id) {
        return clientDao.findById(id);
    }

    public Client delete(UUID id) {
        Optional<Client> clientOptional = clientDao.findById(id);
        if (clientOptional.isEmpty()) {
            throw new IllegalArgumentException("Client with this id not found.");
        }
        Client client = clientOptional.get();
        clientDao.delete(client);
        return client;
    }

    public List<Client> findAllClients() {
        return clientDao.getAllClients();
    }

    public List<Client> findAllStudents() {
        return clientDao.getAllStudents();
    }

    public List<Client> findAllNonStudents() {
        return clientDao.getAllNonStudents();
    }

    public void update(Client client) {
        clientDao.update(client);
    }
}
