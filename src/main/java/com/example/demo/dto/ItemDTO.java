package com.example.demo.dto;


public class ItemDTO {
    private String itemId;
    private int qty;

    public ItemDTO() { }

    public ItemDTO(String itemId, int qty) {
        this.itemId = itemId;
        this.qty = qty;
    }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
}
