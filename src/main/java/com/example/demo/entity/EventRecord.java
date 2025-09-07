package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;


@Entity
@Table(name = "event_records")
public class EventRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", length = 128)
    private String eventId;

    @Column(name = "event_type", length = 64)
    private String eventType;

    private Instant timestamp;

    @Lob
    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public EventRecord() { }

    public EventRecord(String eventId, String eventType, Instant timestamp, String payload) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    
    public Long getId() { return id; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
}
