package com.example.demo.dto;

import java.time.Instant;

public class OrderCancelledDTO {
    private String eventId;
    private Instant timestamp;
    private String eventType;

    private String orderId;
    private String reason;

    public OrderCancelledDTO() { }

   
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
