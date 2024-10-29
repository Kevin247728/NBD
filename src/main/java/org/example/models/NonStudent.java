package org.example.models;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class NonStudent extends ClientType {

    @BsonProperty("additionalFee")
    private float additionalFee = 5;

    @BsonCreator
    public NonStudent() {
        super(2, 15);
    }

    public float getAdditionalFee() {
        return additionalFee;
    }
}