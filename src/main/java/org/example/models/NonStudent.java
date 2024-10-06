package org.example.models;

public class NonStudent extends ClientType {
    private float additionalFee;

    public NonStudent(int maxRentDays, float additionalFee) {
        super(2, maxRentDays); // NonStudent mogą wypożyczać maksymalnie 2 książki
        this.additionalFee = additionalFee;
    }

    public float getAdditionalFee() {
        return additionalFee;
    }
}