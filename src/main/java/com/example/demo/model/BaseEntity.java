package com.example.demo.model;

import java.util.Date;
import java.util.UUID;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
     @Column(updatable = false)
    private Date createdAt;

    @Nullable
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @PrePersist
    public void prePersist() {
        Date now = new Date();
        createdAt = now;  // Set createdAt only when the entity is first created
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = new Date();  
    }

    public BaseEntity() {
    }
    public BaseEntity(UUID id, Date createdAt, Date updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
