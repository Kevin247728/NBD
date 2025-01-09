package org.example.repositories;

import org.example.models.Client;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {
    Optional<Client> findById(UUID id);
    List<Client> findAllClients();
    List<Client> findAllStudents();
    List<Client> findAllNonStudents();
    Client createStudent(UUID id, String firstName, String lastName, String discriminator, int maxBooks, int maxRentDays);
    Client createNonStudent(UUID id, float additionalFee, String firstName, String lastName, String discriminator, int maxBooks, int maxRentDays);
    Client delete(UUID id);
    void update(Client client);
}
