//package org.example.Codecs;
//
//import org.bson.codecs.Codec;
//import org.bson.codecs.configuration.CodecProvider;
//import org.bson.codecs.configuration.CodecRegistry;
//import org.example.models.UniqueIdMgd;
//
//public class UniqueIdCodecProvider implements CodecProvider {
//
//    public UniqueIdCodecProvider() {
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
//        if (clazz == UniqueIdMgd.class) {
//            return (Codec<T>) new UniqueIdCodec(registry);
//        }
//        return null;
//    }
//
//}
