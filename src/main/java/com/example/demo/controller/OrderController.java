package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.dto.OrderCancelledDTO;
import com.example.demo.dto.OrderCreatedDTO;
import com.example.demo.dto.PaymentReceivedDTO;
import com.example.demo.dto.ShippingScheduledDTO;
import com.example.demo.service.EventProcessor;
import com.example.demo.repository.OrderRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final EventProcessor processor;
    private final OrderRepository orderRepository;
    private final ObjectMapper mapper;

    public OrderController(EventProcessor processor, OrderRepository orderRepository, ObjectMapper mapper) {
        this.processor = processor;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @PostMapping("/events")
    public Order processEvent(@RequestBody JsonNode json) {
        try {
            String type = json.has("eventType") ? json.get("eventType").asText() : "";
            Object event = null;

            if ("OrderCreated".equalsIgnoreCase(type)) {
                event = mapper.treeToValue(json, OrderCreatedDTO.class);
            } else if ("PaymentReceived".equalsIgnoreCase(type)) {
                event = mapper.treeToValue(json, PaymentReceivedDTO.class);
            } else if ("ShippingScheduled".equalsIgnoreCase(type)) {
                event = mapper.treeToValue(json, ShippingScheduledDTO.class);
            } else if ("OrderCancelled".equalsIgnoreCase(type)) {
                event = mapper.treeToValue(json, OrderCancelledDTO.class);
            }

            if (event != null && json.has("timestamp")) {
                Instant ts = Instant.parse(json.get("timestamp").asText());
                if (event instanceof OrderCreatedDTO) ((OrderCreatedDTO) event).setTimestamp(ts);
                if (event instanceof PaymentReceivedDTO) ((PaymentReceivedDTO) event).setTimestamp(ts);
                if (event instanceof ShippingScheduledDTO) ((ShippingScheduledDTO) event).setTimestamp(ts);
                if (event instanceof OrderCancelledDTO) ((OrderCancelledDTO) event).setTimestamp(ts);
            }

            return processor.processEvent(event);
        } catch (Exception e) {
            System.out.println("Error while processing event: " + e.getMessage());
            return null;
        }
    }

    @GetMapping("/orders")
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/orders/{id}")
    public Order getOrder(@PathVariable String id) {
        return orderRepository.findById(id).orElse(null);
    }
}
