package org.example.models;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import org.example.exceptions.UnavailableException;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        // Cast the object to Book and compare entityId
        Book book = (Book) o;
        return Objects.equals(getEntityId(), book.getEntityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntityId());
    }
}

