//package org.example.Codecs;
//
//import org.bson.codecs.Codec;
//import org.bson.codecs.configuration.CodecProvider;
//import org.bson.codecs.configuration.CodecRegistry;
//import org.example.models.ClientType;
//
//public class ClientTypeCodecProvider implements CodecProvider {
//
//    public ClientTypeCodecProvider() {
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
//        if (clazz == ClientType.class) {
//            return (Codec<T>) new ClientTypeCodec(registry);
//        }
//        return null;
//    }
//
//}
