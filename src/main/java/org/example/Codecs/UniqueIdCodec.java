package org.example.Codecs;

import org.bson.BsonBinary;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.example.models.UniqueIdMgd;

import java.util.UUID;

public class UniqueIdCodec implements Codec<UniqueIdMgd> {

    private Codec<UniqueIdMgd> UniqueIdCodec;

    public UniqueIdCodec(CodecRegistry registry) {
        this.UniqueIdCodec = registry.get(UniqueIdMgd.class);
    }

    @Override
    public UniqueIdMgd decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument(); // Rozpoczynamy wczytywanie dokumentu
        UUID id = bsonReader.readBinaryData("_id").asUuid(); // Wczytujemy UUID
        bsonReader.readEndDocument(); // Kończymy wczytywanie dokumentu
        return new UniqueIdMgd(id); // Tworzymy obiekt UniqueIdMgd z wczytanym UUID
    }

    @Override
    public void encode(BsonWriter bsonWriter, UniqueIdMgd value, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument(); // Rozpoczynamy zapis dokumentu
        bsonWriter.writeBinaryData("_id", new BsonBinary(value.getId())); // Zapisujemy UUID jako dane binarne
        bsonWriter.writeEndDocument(); // Kończymy zapis dokumentu
    }

    @Override
    public Class<UniqueIdMgd> getEncoderClass() {
        return UniqueIdMgd.class;
    }
}
