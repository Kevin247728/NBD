package org.example.models;

import org.example.exceptions.UnavailableException;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private int id;
    private String title;
    private boolean isRented = false;
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
        if (isRented) {
            throw new UnavailableException("Book is already rented");
        }
        isRented = true;
    }

    public void returnBook() {
        isRented = false;
    }
}

