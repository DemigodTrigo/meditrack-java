package com.airtribe.meditrack.entity;

import java.time.LocalDateTime;

public abstract class MedicalEntity {

    private final String entityId;
    private final LocalDateTime createdAt;

    protected boolean active;

    protected MedicalEntity(String entityId) {
        this.entityId = entityId;
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    public final String getEntityId() {
        return entityId;
    }

    public final LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public abstract String getEntityType();

    @Override
    public String toString() {
        return getEntityType() +
                "{entityId='" + entityId + '\'' +
                ", active=" + active +
                '}';
    }

}
