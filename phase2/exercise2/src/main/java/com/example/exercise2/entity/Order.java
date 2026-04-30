package com.example.exercise2.entity;

import com.example.exercise2.utils.Status;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "user_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "order_name")
    private String orderName;
    @Column(name = "sub_total")
    private Double subTotal;
    private Double total;
    @Enumerated(EnumType.STRING)
    private Status status;
    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "creation_timestamp")
    private Timestamp creationTimestamp;
    @Column(name = "last_updated_timestamp")
    @UpdateTimestamp(source = SourceType.DB)
    private Timestamp lastUpdatedTimestamp;
    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public String getOrderName() {
        return orderName;
    }

    public Double getTotal() {
        return total;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public Timestamp getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
