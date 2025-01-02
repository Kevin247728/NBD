//package org.example.Codecs;
//
//import org.bson.*;
//import org.bson.codecs.*;
//import org.bson.codecs.configuration.CodecRegistry;
//import org.example.exceptions.TooManyException;
//import org.example.models.Rent;
//import org.example.models.UniqueIdMgd;
//
//import java.time.LocalDate;
//
//public class RentCodec implements Codec<Rent> {
//
//    private final Codec<UniqueIdMgd> uniqueIdCodec; // do kodowania/odkodowania pól clientId i bookId
//    private final Codec<Rent> rentCodec; // kodowanie całego Rent
//
//    public RentCodec(CodecRegistry registry) {
//        this.uniqueIdCodec = registry.get(UniqueIdMgd.class); // Używamy kodeka UniqueIdMgd
//        this.rentCodec = registry.get(Rent.class); // Przyjmujemy rentCodec, jeśli będzie potrzebny do innych części dokumentu
//    }
//
//    @Override
//    public Rent decode(BsonReader bsonReader, DecoderContext decoderContext) {
//        bsonReader.readStartDocument();
//
//        bsonReader.readName("_id");
//        UniqueIdMgd uniqueIdMgd = uniqueIdCodec.decode(bsonReader, decoderContext);
//        bsonReader.readName("clientId");
//        UniqueIdMgd clientId = uniqueIdCodec.decode(bsonReader, decoderContext);
//
//        bsonReader.readName("bookId");
//        UniqueIdMgd bookId = uniqueIdCodec.decode(bsonReader, decoderContext);
//
//        LocalDate beginDate = LocalDate.parse(bsonReader.readString("beginDate"));
//        LocalDate endDate = LocalDate.parse(bsonReader.readString("endDate"));
//        float fee = (float) bsonReader.readDouble("fee");
//
//        bsonReader.readEndDocument();
//
//        Rent rent = null;
//        try {
//            rent = new Rent(clientId, bookId, beginDate, endDate);
//        } catch (TooManyException e) {
//            throw new RuntimeException(e);
//        }
//        rent.setFee(fee);
//        rent.setEntityId(uniqueIdMgd);
//
//        return rent;
//    }
//
//    @Override
//    public void encode(BsonWriter bsonWriter, Rent value, EncoderContext encoderContext) {
//        bsonWriter.writeStartDocument();
//
//        bsonWriter.writeName("_id");
//        uniqueIdCodec.encode(bsonWriter, value.getEntityId(), encoderContext);
//        bsonWriter.writeName("clientId");
//        uniqueIdCodec.encode(bsonWriter, value.getClientId(), encoderContext);
//
//        bsonWriter.writeName("bookId");
//        uniqueIdCodec.encode(bsonWriter, value.getBookId(), encoderContext);
//
//        bsonWriter.writeString("beginDate", value.getBeginDate().toString());
//        bsonWriter.writeString("endDate", value.getEndDate().toString());
//        bsonWriter.writeDouble("fee", value.getFee());
//
//        bsonWriter.writeEndDocument();
//    }
//
//    @Override
//    public Class<Rent> getEncoderClass() {
//        return Rent.class;
//    }
//}
