package org.example.models;

import jakarta.persistence.*;
import org.example.exceptions.UnavailableException;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private boolean isRented = false;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    public Book(int id, String title, HashSet<Author> authors) {
        this.id = id;
        this.title = title;
        this.authors = authors;
    }

    public Book() {}

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Set<Author> getAuthor() {
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

