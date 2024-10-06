package org.example.models;

public class Student extends ClientType {
    public Student(int maxRentDays) {
        super(3, maxRentDays); // Studenci mogą wypożyczać maksymalnie 3 książki
    }
}