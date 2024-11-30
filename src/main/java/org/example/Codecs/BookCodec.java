package org.example.Codecs;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.example.models.*;

public class BookCodec implements Codec<Book> {
    private final Codec<Book> bookCodec;
    private final Codec<UniqueIdMgd> uniqueIdCodec;

    public BookCodec(CodecRegistry registry) {
        this.bookCodec = registry.get(Book.class);
        this.uniqueIdCodec = registry.get(UniqueIdMgd.class);
    }

    @Override
    public Book decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();

        bsonReader.readName("_id");
        UniqueIdMgd uniqueIdMgd = uniqueIdCodec.decode(bsonReader, decoderContext);
        String title = bsonReader.readString("title");
        boolean isRented = bsonReader.readBoolean("isRented");

        bsonReader.readEndDocument();

        Book book = new Book(title);
        book.setEntityId(uniqueIdMgd);
        book.setRented(isRented);
        return book;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Book book, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();

        bsonWriter.writeName("_id");
        uniqueIdCodec.encode(bsonWriter, book.getEntityId(), encoderContext);
        bsonWriter.writeString("title", book.getTitle());

        bsonWriter.writeName("isRented");
        bsonWriter.writeBoolean(book.isRented());

        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<Book> getEncoderClass() {
        return Book.class;
    }
}
