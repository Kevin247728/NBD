package org.example.DAO;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import org.example.models.Client;
import org.example.models.NonStudent;
import org.example.models.Student;
import org.example.providers.ClientQueryProvider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Dao
public interface ClientDao {

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {Student.class, NonStudent.class})
    Optional<Client> findById(UUID id);

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {Student.class, NonStudent.class})
    Client create(Client client);

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {Student.class, NonStudent.class})
    Client delete(Client client);

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {Student.class, NonStudent.class})
    List<Client> getAllClients();

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {Student.class, NonStudent.class})
    List<Client> getAllNonStudents();

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {Student.class, NonStudent.class})
    List<Client> getAllStudents();

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {Student.class, NonStudent.class})
    void update(Client client);
}
