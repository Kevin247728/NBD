package org.example.models;


import jakarta.persistence.*;

@Entity
@DiscriminatorValue("NONSTUDENT")
public class NonStudent extends ClientType {
    private float additionalFee = 5;

    public NonStudent() {
        super(2, 15);
    }

    public float getAdditionalFee() {
        return additionalFee;
    }
}