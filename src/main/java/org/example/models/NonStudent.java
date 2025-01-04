package org.example.models;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import com.datastax.oss.driver.api.mapper.entity.naming.GetterStyle;

@Entity(defaultKeyspace = "rent_a_book")
@CqlName("clients")
@PropertyStrategy(mutable = false, getterStyle = GetterStyle.JAVABEANS)
public class NonStudent extends Client {

    @CqlName("additional_fee")
    private float additionalFee;

    public NonStudent(String firstName, String lastName, float additionalFee) {
        super(firstName, lastName, "NonStudent", 3, 15);
        this.additionalFee = additionalFee;
    }

    public float getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(float additionalFee) {
        this.additionalFee = additionalFee;
    }

    @Override
    public String toString() {
        return "NonStudent{" +
                "firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", maxBooks=" + getMaxBooks() +
                ", maxRentDays=" + getMaxRentDays() +
                ", additionalFee=" + additionalFee +
                '}';
    }
}