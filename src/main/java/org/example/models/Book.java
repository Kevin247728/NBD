package org.example.models;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import org.example.exceptions.UnavailableException;

@Entity
public class Book extends AbstractEntity {

    @NotNull
    private String title;

    public Book(String title) {
        this.title = title;
    }

    public Book() {}

    public String getTitle() {
        return title;
    }
}

