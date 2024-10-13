package org.example.models;

import com.sun.istack.NotNull;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class AbstractEntity {

    @EmbeddedId
    @AttributeOverride(name = "id", column = @Column(name = "entity_id"))
    @NotNull
    private UniqueId entityId;

    @Version
    private long version;

    public UniqueId getEntityId() {
        return entityId;
    }
}
