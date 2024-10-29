package org.example.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public abstract class ClientType extends AbstractEntityMgd {

    @BsonProperty("maxBooks")
    private int maxBooks;

    @BsonProperty("maxRentDays")
    private int maxRentDays;

    @BsonCreator
    public ClientType(@BsonProperty("maxBooks") int maxBooks,
                      @BsonProperty("maxRentDays") int maxRentDays) {
        super();
        this.maxBooks = maxBooks;
        this.maxRentDays = maxRentDays;
    }

    public int getMaxBooks() {
        return maxBooks;
    }

    public int getMaxRentDays() {
        return maxRentDays;
    }
}

