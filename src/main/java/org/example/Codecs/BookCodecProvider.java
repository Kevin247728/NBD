package org.example.Codecs;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.example.models.Book;
import org.example.models.Client;

public class BookCodecProvider implements CodecProvider {

    public BookCodecProvider() {}

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == Book.class) {
            return (Codec<T>) new BookCodec(registry);
        }
        return null;
    }
}
