package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;


@Embeddable
public class OrderItem {

    @Column(name = "item_id", length = 64)
    private String itemId;

    @Column(name = "qty")
    private int qty;

    public OrderItem() { }

    public OrderItem(String itemId, int qty) {
        this.itemId = itemId;
        this.qty = qty;
    }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
}
