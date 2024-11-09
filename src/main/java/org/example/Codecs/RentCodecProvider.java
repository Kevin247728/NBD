package org.example.Codecs;

import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.example.models.Rent;

public class RentCodecProvider implements CodecProvider {

    public RentCodecProvider() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == Rent.class) {
            return (Codec<T>) new RentCodec(registry);
        }
        return null;
    }
}
