package org.example.models;

import jakarta.persistence.*;
import org.example.exceptions.UnavailableException;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private boolean isRented = false;

    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors = new ArrayList<>();

    public Book(int id, String title, List<Author> authors) {
        this.id = id;
        this.title = title;
        this.authors = authors;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Author> getAuthor() {
        return authors;
    }

    public boolean isRented() {
        return isRented;
    }

    public void rentBook() throws UnavailableException {
        isRented = true;
    }

    public void returnBook() {
        isRented = false;
    }
}

