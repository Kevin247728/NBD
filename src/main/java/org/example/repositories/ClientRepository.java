package org.example.repositories;

import org.example.models.Client;
import java.util.List;
import java.util.UUID;

public interface ClientRepository {
    Client findById(UUID id);
    List<Client> findAllClients();
    List<Client> findAllStudents();
    List<Client> findAllNonStudents();
    void createStudent(UUID id, String firstName, String lastName, String discriminator, int maxBooks, int maxRentDays);
    void createNonStudent(UUID id, float additionalFee, String firstName, String lastName, String discriminator, int maxBooks, int maxRentDays);
    Client delete(UUID id);
    void update(Client client);
}
