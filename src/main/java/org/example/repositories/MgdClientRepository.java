package org.example.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.conversions.Bson;
import org.example.models.Client;
import org.example.models.UniqueIdMgd;

import java.util.ArrayList;
import java.util.List;

public class MgdClientRepository implements ClientRepository {

    private final MongoCollection<Client> clientCollection;

    public MgdClientRepository(MongoDatabase database) {
        this.clientCollection = database.getCollection("clients", Client.class);
    }

    @Override
    public void create(Client client) {
        clientCollection.insertOne(client);
    }

    @Override
    public Client findById(UniqueIdMgd id) {
        return clientCollection.find(Filters.eq("_id", id)).first();
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        clientCollection.find().into(clients);
        return clients;
    }

    @Override
    public boolean update(Client client) {
        Bson filter = Filters.eq("_id", client.getEntityId());
        ReplaceOptions options = new ReplaceOptions().upsert(false);
        return clientCollection.replaceOne(filter, client, options).getModifiedCount() > 0;
    }

    @Override
    public boolean delete(Client client) {
        return clientCollection.deleteOne(Filters.eq("_id", client.getEntityId())).getDeletedCount() > 0;
    }
}