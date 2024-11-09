package org.example.Codecs;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.example.models.Client;

public class ClientCodecProvider implements CodecProvider {

    public ClientCodecProvider() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == Client.class) {
            return (Codec<T>) new ClientCodec(registry);
        }
        return null;
    }
}
