package org.example.models;


import jakarta.persistence.*;

@Entity
@DiscriminatorValue("STUDENT")
public class Student extends ClientType {
    public Student() {
        super(3, 20);
    }
}