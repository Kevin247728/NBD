package org.example.models;

import org.bson.codecs.pojo.annotations.BsonId;

import java.util.UUID;

public class UniqueIdMgd {

    @BsonId
    private UUID id;

    public UniqueIdMgd() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniqueIdMgd that = (UniqueIdMgd) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

