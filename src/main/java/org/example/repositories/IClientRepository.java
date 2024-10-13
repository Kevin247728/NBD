package org.example.repositories;

import org.example.models.Client;

import java.util.List;

public interface IClientRepository {
    Client findById(Long id);
    List<Client> findAll();
    void save(Client client);
    void delete(Client client);
}
