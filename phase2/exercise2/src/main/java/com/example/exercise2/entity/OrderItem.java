package com.example.exercise2.entity;

import com.example.exercise2.utils.Status;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private int qty;
    @Column(name = "sum_price")
    private Double sumPrice;
    @Column(name = "sub_total")
    private Double subTotal;
    @Enumerated(EnumType.STRING)
    private Status status;
    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "creation_timestamp")
    private Timestamp creationTimestamp;
    @Column(name = "last_updated_timestamp")
    @UpdateTimestamp(source = SourceType.DB)
    private Timestamp lastUpdatedTimestamp;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    public Double getSubTotal() {
        return subTotal;
    }

    public Double getSumPrice() {
        return sumPrice;
    }

    public int getId() {
        return id;
    }

    public int getQty() {
        return qty;
    }

    public Status getStatus() {
        return status;
    }

    public Order getOrder() {
        return order;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public Timestamp getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public void setSumPrice(Double sumPrice) {
        this.sumPrice = sumPrice;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
