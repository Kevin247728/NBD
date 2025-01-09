package org.example.models;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import com.datastax.oss.driver.api.mapper.entity.naming.GetterStyle;
import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_book")
@CqlName("clients")
@PropertyStrategy(mutable = false, getterStyle = GetterStyle.JAVABEANS)
public class Student extends Client {

    public Student(UUID id, String firstName, String lastName, String discriminator, int maxBooks, int maxRentDays) {
        super(id, firstName, lastName, discriminator, maxBooks, maxRentDays);
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", maxBooks=" + getMaxBooks() +
                ", maxRentDays=" + getMaxRentDays() +
                '}';
    }
}