package com.example.exercise2.entity;

import com.example.exercise2.utils.Status;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private Double price;
    @Column(name = "available_units")
    private int availableUnits;
    @Enumerated(EnumType.STRING)
    private Status status;
    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "creation_timestamp")
    private Timestamp creationTimestamp;
    @Column(name = "last_updated_timestamp")
    @UpdateTimestamp(source = SourceType.DB)
    private Timestamp lastUpdatedTimestamp;

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getAvailableUnits() {
        return availableUnits;
    }

    public Timestamp getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setAvailableUnits(int availableUnits) {
        this.availableUnits = availableUnits;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
