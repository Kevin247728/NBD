package org.example.models;

import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class UniqueId {

    private UUID id;

    public UniqueId() {}
}