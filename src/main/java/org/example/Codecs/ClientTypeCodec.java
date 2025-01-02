//package org.example.Codecs;
//
//import org.bson.BsonReader;
//import org.bson.BsonWriter;
//import org.bson.codecs.Codec;
//import org.bson.codecs.DecoderContext;
//import org.bson.codecs.EncoderContext;
//import org.bson.codecs.configuration.CodecRegistry;
//import org.example.models.ClientType;
//import org.example.models.NonStudent;
//import org.example.models.Student;
//
//public class ClientTypeCodec implements Codec<ClientType> {
//
//    private Codec<ClientType> clientTypeCodec;
//
//    public ClientTypeCodec(CodecRegistry registry) {
//        this.clientTypeCodec = registry.get(ClientType.class);
//    }
//
//    @Override
//    public ClientType decode(BsonReader bsonReader, DecoderContext decoderContext) {
//        bsonReader.readStartDocument();
//
//        int maxBooks = bsonReader.readInt32("maxBooks");
//        int maxRentDays = bsonReader.readInt32("maxRentDays");
//        String type = bsonReader.readString("type");
//
//        ClientType clientType = null;
//
//        if ("Student".equals(type)) {
//            clientType = new Student(maxBooks, maxRentDays);
//        } else if ("NonStudent".equals(type)) {
//            float additionalFee = (float) bsonReader.readDouble("additionalFee");
//            clientType = new NonStudent(maxBooks, maxRentDays, additionalFee);
//        } else {
//            throw new IllegalArgumentException("Unknown ClientType: " + type);
//        }
//
//        bsonReader.readEndDocument();
//        return clientType;
//
//    }
//
//    @Override
//    public void encode(BsonWriter bsonWriter, ClientType value, EncoderContext encoderContext) {
//        bsonWriter.writeStartDocument();
//
//        bsonWriter.writeInt32("maxBooks", value.getMaxBooks());
//        bsonWriter.writeInt32("maxRentDays", value.getMaxRentDays());
//
//        if (value instanceof Student) {
//            bsonWriter.writeString("type", "Student");
//        } else if (value instanceof NonStudent) {
//            bsonWriter.writeString("type", "NonStudent");
//            bsonWriter.writeDouble("additionalFee", ((NonStudent) value).getAdditionalFee());
//        }
//
//        bsonWriter.writeEndDocument();
//    }
//
//    @Override
//    public Class<ClientType> getEncoderClass() {
//        return ClientType.class;
//    }
//}
