//package org.example.repositories;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoDatabase;
//import org.bson.UuidRepresentation;
//import org.bson.codecs.Codec;
//import org.bson.codecs.configuration.CodecRegistries;
//import org.bson.codecs.configuration.CodecRegistry;
//import org.bson.codecs.pojo.Conventions;
//import org.bson.codecs.pojo.PojoCodecProvider;
//import org.example.Codecs.*;
//
//import java.util.List;
//
//public abstract class AbstractMongoRepository implements AutoCloseable {
//
//    protected ConnectionString connectionString = new ConnectionString(
//            "mongodb://127.0.0.1:27017,127.0.0.1:27018,127.0.0.1:27019/?replicaSet=rs0");
//    protected MongoClient mongoClient;
//    protected MongoDatabase mongoDatabase;
//
//    private CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
//            PojoCodecProvider.builder()
//                    .automatic(true)
//                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
//                    .build());
//
//    private void initDbConnection() {
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .applyConnectionString(connectionString)
//                .uuidRepresentation(UuidRepresentation.STANDARD)
//                .codecRegistry(CodecRegistries.fromRegistries(
//                        CodecRegistries.fromProviders(new ClientTypeCodecProvider()),
//                        CodecRegistries.fromProviders(new UniqueIdCodecProvider()),
//                        CodecRegistries.fromProviders(new RentCodecProvider()),
//                        CodecRegistries.fromProviders(new ClientCodecProvider()),
//                        CodecRegistries.fromProviders(new BookCodecProvider()),
//                        MongoClientSettings.getDefaultCodecRegistry(),
//                        pojoCodecRegistry
//                ))
//                .build();
//
//        mongoClient = MongoClients.create(settings);
//        mongoDatabase = mongoClient.getDatabase("rs0");
//    }
//
//    public AbstractMongoRepository() {
//        initDbConnection();
//    }
//
//    @Override
//    public void close() {
//        if (mongoClient != null) {
//            mongoClient.close();
//        }
//    }
//
//    public MongoDatabase getMongoDatabase() {
//        return mongoDatabase;
//    }
//
//
//}