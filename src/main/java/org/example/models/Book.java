package org.example.models;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;


public class Book extends AbstractEntityMgd {

    @BsonProperty("title")
    private String title;

    @BsonCreator
    public Book(@BsonProperty("title") String title) {
        super();
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {this.title = title;}
}

