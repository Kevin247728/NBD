package org.example.models;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import com.datastax.oss.driver.api.mapper.entity.naming.GetterStyle;

import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_book")
@CqlName("books")
@PropertyStrategy(mutable = false, getterStyle = GetterStyle.JAVABEANS)
public class Book {

    @PartitionKey
    private UUID id;

    private String title;

    @CqlName("is_rented")
    private boolean isRented;

    public Book(UUID id, String title, boolean isRented) {
        this.title = title;
        this.isRented = isRented;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {this.title = title;}

    public UUID getId() {
        return id;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }
}

