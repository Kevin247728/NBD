package org.example.models;
import org.bson.codecs.pojo.annotations.BsonProperty;

public abstract class AbstractEntityMgd {

    @BsonProperty("_id")
    private final UniqueIdMgd entityId;

    public AbstractEntityMgd() {
        this.entityId = new UniqueIdMgd();
    }

    public AbstractEntityMgd(UniqueIdMgd entityId) {
        this.entityId = entityId;
    }

    public UniqueIdMgd getEntityId() {
        return entityId;
    }
}
