package org.example.models;


import jakarta.persistence.*;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int authorID;
    private String name;
    private String surname;

    public Author(int authorID, String name, String surname) {
        this.authorID = authorID;
        this.name = name;
        this.surname = surname;
    }

    public Author() {}

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

