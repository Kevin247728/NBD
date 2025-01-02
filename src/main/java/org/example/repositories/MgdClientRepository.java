//package org.example.repositories;
//
//import com.mongodb.client.ClientSession;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.model.ReplaceOptions;
//import org.bson.conversions.Bson;
//import org.example.models.Client;
//import org.example.models.UniqueIdMgd;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MgdClientRepository extends AbstractMongoRepository implements ClientRepository {
//
//    private final MongoCollection<Client> clientCollection;
//
//    public MgdClientRepository() {
//        this.clientCollection = mongoDatabase.getCollection("clients", Client.class);
//    }
//
//    @Override
//    public void create(Client client) {
//        try (ClientSession clientSession = mongoClient.startSession()) {
//            clientSession.startTransaction();
//            clientCollection.insertOne(clientSession, client);
//            clientSession.commitTransaction();
//        } catch (RuntimeException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public Client findById(UniqueIdMgd id) {
//        return clientCollection.find(Filters.eq("_id", id)).first();
//    }
//
//    @Override
//    public List<Client> findAll() {
//        List<Client> clients = new ArrayList<>();
//        clientCollection.find().into(clients);
//        return clients;
//    }
//
//    @Override
//    public boolean update(Client client) {
//        try (ClientSession clientSession = mongoClient.startSession()) {
//            clientSession.startTransaction();
//            Bson filter = Filters.eq("_id", client.getEntityId());
//            ReplaceOptions options = new ReplaceOptions().upsert(false);
//            boolean updateSucceeded =  clientCollection.replaceOne(clientSession, filter, client, options).getModifiedCount() > 0;
//            clientSession.commitTransaction();
//            return updateSucceeded;
//        } catch (RuntimeException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public boolean delete(Client client) {
//        try (ClientSession clientSession = mongoClient.startSession()) {
//            clientSession.startTransaction();
//            boolean deleteSucceeded =  clientCollection.deleteOne(clientSession, Filters.eq("_id", client.getEntityId())).getDeletedCount() > 0;
//            clientSession.commitTransaction();
//            return deleteSucceeded;
//        } catch (RuntimeException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}