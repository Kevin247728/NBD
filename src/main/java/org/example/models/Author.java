package org.example.models;

public class Author {
    private int authorID;
    private String name;
    private String surname;

    public Author(int authorID, String name, String surname) {
        this.authorID = authorID;
        this.name = name;
        this.surname = surname;
    }

    public int getAuthorID() {
        return authorID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}

