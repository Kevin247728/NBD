package org.example.Codecs;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.example.models.Client;
import org.example.models.ClientType;
import org.example.models.Rent;
import org.example.models.UniqueIdMgd;

import java.util.ArrayList;
import java.util.List;

public class ClientCodec implements Codec<Client> {

    private final Codec<Client> clientCodec;
    private final Codec<ClientType> clientTypeCodec;
    private final Codec<Rent> rentCodec;
    private final Codec<UniqueIdMgd> uniqueIdCodec;

    public ClientCodec(CodecRegistry registry) {
        this.clientCodec = registry.get(Client.class);
        this.clientTypeCodec = registry.get(ClientType.class);
        this.rentCodec = registry.get(Rent.class);
        this.uniqueIdCodec = registry.get(UniqueIdMgd.class);
    }

    @Override
    public Client decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();

        bsonReader.readName("_id");
        UniqueIdMgd uniqueIdMgd = uniqueIdCodec.decode(bsonReader, decoderContext);
        String firstName = bsonReader.readString("firstName");
        String lastName = bsonReader.readString("lastName");

        bsonReader.readName("clientType");
        ClientType clientType = clientTypeCodec.decode(bsonReader, decoderContext);

        List<Rent> rents = new ArrayList<>();
        bsonReader.readName("rents");
        bsonReader.readStartArray();
        while (bsonReader.readBsonType() != org.bson.BsonType.END_OF_DOCUMENT) {
            rents.add(rentCodec.decode(bsonReader, decoderContext));
        }
        bsonReader.readEndArray();
        bsonReader.readEndDocument();

        Client client = new Client(firstName, lastName, clientType);
        client.setEntityId(uniqueIdMgd);
        client.setRents(rents);
        return client;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Client client, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();

        bsonWriter.writeName("_id");
        uniqueIdCodec.encode(bsonWriter, client.getEntityId(), encoderContext);
        bsonWriter.writeString("firstName", client.getFirstName());
        bsonWriter.writeString("lastName", client.getLastName());

        bsonWriter.writeName("clientType");
        clientTypeCodec.encode(bsonWriter, client.getClientType(), encoderContext);

        bsonWriter.writeName("rents");
        bsonWriter.writeStartArray();
        for (Rent rent : client.getRents()) {
            rentCodec.encode(bsonWriter, rent, encoderContext);
        }
        bsonWriter.writeEndArray();

        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<Client> getEncoderClass() {
        return Client.class;
    }
}