package org.example.repositories;

import org.example.models.Client;

import java.util.List;
import java.util.UUID;

public interface IClientRepository {
    Client findById(UUID id);
    List<Client> findAll();
    void save(Client client);
    void delete(Client client);
}
