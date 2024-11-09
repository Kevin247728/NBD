package org.example.models;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;


public class Book extends AbstractEntityMgd {

    @BsonProperty("title")
    private String title;

    @BsonProperty("isRented")
    private boolean isRented;

    @BsonCreator
    public Book(@BsonProperty("title") String title) {
        super();
        this.title = title;
        this.isRented = false;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {this.title = title;}

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }
}

