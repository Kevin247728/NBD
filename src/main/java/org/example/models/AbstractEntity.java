package org.example.models;

import com.sun.istack.NotNull;
import jakarta.persistence.*;

import java.util.UUID;

@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "entity_id", updatable = false, nullable = false)
    private UUID entityId;

    @Version
    private long version;

    public UUID getEntityId() {
        return entityId;
    }
}
