package org.example.models;
import org.bson.codecs.pojo.annotations.BsonCreator;

public class Student extends ClientType {

    @BsonCreator
    public Student() {
        super(3, 20);
    }
}