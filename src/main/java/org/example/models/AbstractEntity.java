package org.example.models;

import com.sun.istack.NotNull;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class AbstractEntity {

    @Embedded
    @NotNull
    private UniqueId entityId;

    @Version
    private long version;
}
