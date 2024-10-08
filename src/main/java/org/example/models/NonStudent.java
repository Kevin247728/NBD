package org.example.models;

public class NonStudent extends ClientType {
    private float additionalFee;

    public NonStudent(float additionalFee) {
        super(2, 15);
        this.additionalFee = additionalFee;
    }

    public float getAdditionalFee() {
        return additionalFee;
    }
}