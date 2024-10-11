package org.example.models;


import jakarta.persistence.*;

@Entity
@DiscriminatorValue("NONSTUDENT")
public class NonStudent extends ClientType {
    private float additionalFee;

    public NonStudent(float additionalFee) {
        super(2, 15);
        this.additionalFee = additionalFee;
    }

    public NonStudent() {}

    public float getAdditionalFee() {
        return additionalFee;
    }
}