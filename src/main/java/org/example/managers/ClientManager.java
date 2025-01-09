package org.example.managers;

import org.example.models.Client;
import org.example.repositories.CassandraClientRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientManager {

    private final CassandraClientRepository clientRepository;

    public ClientManager(CassandraClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createStudent(String firstName, String lastName, int maxBooks, int maxRentDays) {
        UUID id = UUID.randomUUID();
        String discriminator = "Student";
        return clientRepository.createStudent(id, firstName, lastName, discriminator, maxBooks, maxRentDays);
    }

    public Client createNonStudent(String firstName, String lastName, float additionalFee, int maxBooks, int maxRentDays) {
        UUID id = UUID.randomUUID();
        String discriminator = "NonStudent";
        return clientRepository.createNonStudent(id, additionalFee, firstName, lastName, discriminator, maxBooks, maxRentDays);
    }

    public Optional<Client> getClientById(UUID id) {
        return clientRepository.findById(id);
    }

    public Client removeClient(UUID id) {
        return clientRepository.delete(id);
    }

    public List<Client> listAllClients() {
        return clientRepository.findAllClients();
    }

    public List<Client> listAllStudents() {
        return clientRepository.findAllStudents();
    }

    public List<Client> listAllNonStudents() {
        return clientRepository.findAllNonStudents();
    }

    public void updateClientInfo(Client client) {
        clientRepository.update(client);
    }
}