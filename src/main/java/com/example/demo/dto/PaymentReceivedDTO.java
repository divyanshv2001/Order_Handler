package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.Instant;


public class PaymentReceivedDTO {
    private String eventId;
    private Instant timestamp;
    private String eventType;

    private String orderId;
    private BigDecimal amountPaid;

    public PaymentReceivedDTO() { }

    
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }
}
