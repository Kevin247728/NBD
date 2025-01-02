//package org.example.models;
//import org.bson.codecs.pojo.annotations.BsonCreator;
//import org.bson.codecs.pojo.annotations.BsonDiscriminator;
//import org.bson.codecs.pojo.annotations.BsonProperty;
//
//@BsonDiscriminator(key = "type", value = "Student")
//public class Student extends ClientType {
//
//    @BsonCreator
//    public Student(@BsonProperty("maxBooks") int maxBooks,
//                   @BsonProperty("maxRentDays") int maxRentDays) {
//        super(maxBooks, maxRentDays);
//    }
//
//    public Student() {
//        super(3, 20);
//    }
//}