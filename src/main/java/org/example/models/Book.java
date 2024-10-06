package org.example.models;

public class Book {
    private int id;
    private String title;
    private boolean isRented = false;
    private Author author;

    public Book(int id, String title, Author author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
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

