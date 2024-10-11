package org.example.models;

import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class UniqueId {

    private UUID id;

    public UniqueId() {
        this.id = UUID.randomUUID();
    }

    public UniqueId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}