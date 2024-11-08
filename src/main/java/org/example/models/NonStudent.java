package org.example.models;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator(key = "type", value = "NonStudent")
public class NonStudent extends ClientType {

    @BsonProperty("additionalFee")
    private float additionalFee = 5;

    @BsonCreator
    public NonStudent(@BsonProperty("maxBooks") int maxBooks,
                      @BsonProperty("maxRentDays") int maxRentDays,
                      @BsonProperty("additionalFee") float additionalFee) {
        super(maxBooks, maxRentDays);
        this.additionalFee = additionalFee;
    }

    public NonStudent() {
        super(3, 15);
    }

    public float getAdditionalFee() {
        return additionalFee;
    }
}