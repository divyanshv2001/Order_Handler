package com.example.demo.service;

import com.example.demo.annotation.NotifyObservers;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventProcessor {

    private final OrderRepository orderRepository;
    private final EventRecordRepository eventRecordRepository;
    private final ObjectMapper objectMapper;

    public EventProcessor(OrderRepository orderRepository,
                          EventRecordRepository eventRecordRepository,
                          ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.eventRecordRepository = eventRecordRepository;
        this.objectMapper = objectMapper;
    }

    @NotifyObservers
    @Transactional
    public Order processEvent(Object event) {
        try {
            if (event instanceof OrderCreatedDTO) {
                return handleOrderCreated((OrderCreatedDTO) event);
            } else if (event instanceof PaymentReceivedDTO) {
                return handlePaymentReceived((PaymentReceivedDTO) event);
            } else if (event instanceof ShippingScheduledDTO) {
                return handleShippingScheduled((ShippingScheduledDTO) event);
            } else if (event instanceof OrderCancelledDTO) {
                return handleOrderCancelled((OrderCancelledDTO) event);
            } else {
                System.out.println("WARN: Unknown event type");
                return null;
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return null;
        }
    }

    private Order handleOrderCreated(OrderCreatedDTO dto) throws JsonProcessingException {
        if (orderRepository.existsById(dto.getOrderId())) {
            System.out.println("INFO: Order already exists " + dto.getOrderId());
            return orderRepository.findById(dto.getOrderId()).orElse(null);
        }

        Order order = new Order();
        order.setOrderId(dto.getOrderId());
        order.setCustomerId(dto.getCustomerId());
        order.setTotalAmount(dto.getTotalAmount());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> items = new ArrayList<>();
        if (dto.getItems() != null) {
            for (ItemDTO i : dto.getItems()) {
                items.add(new OrderItem(i.getItemId(), i.getQty()));
            }
        }
        order.setItems(items);

        orderRepository.save(order);
        saveEventRecord(order, dto.getEventId(), "OrderCreated", dto.getTimestamp(), dto);
        return order;
    }

    private Order handlePaymentReceived(PaymentReceivedDTO dto) throws JsonProcessingException {
        Order order = orderRepository.findById(dto.getOrderId()).orElse(null);
        if (order == null) {
            System.out.println("WARN: Payment for unknown order " + dto.getOrderId());
            return null;
        }

        BigDecimal paid = dto.getAmountPaid() == null ? BigDecimal.ZERO : dto.getAmountPaid();
        BigDecimal total = order.getTotalAmount() == null ? BigDecimal.ZERO : order.getTotalAmount();

        if (paid.compareTo(total) >= 0) {
            order.setStatus(OrderStatus.PAID);
        } else if (paid.compareTo(BigDecimal.ZERO) > 0) {
            order.setStatus(OrderStatus.PARTIALLY_PAID);
        }

        orderRepository.save(order);
        saveEventRecord(order, dto.getEventId(), "PaymentReceived", dto.getTimestamp(), dto);
        return order;
    }

    private Order handleShippingScheduled(ShippingScheduledDTO dto) throws JsonProcessingException {
        Order order = orderRepository.findById(dto.getOrderId()).orElse(null);
        if (order == null) {
            System.out.println("WARN: Shipping scheduled for unknown order " + dto.getOrderId());
            return null;
        }

        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
        saveEventRecord(order, dto.getEventId(), "ShippingScheduled", dto.getTimestamp(), dto);
        return order;
    }

    private Order handleOrderCancelled(OrderCancelledDTO dto) throws JsonProcessingException {
        Order order = orderRepository.findById(dto.getOrderId()).orElse(null);
        if (order == null) {
            System.out.println("WARN: Cancel for unknown order " + dto.getOrderId());
            return null;
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        saveEventRecord(order, dto.getEventId(), "OrderCancelled", dto.getTimestamp(), dto);
        return order;
    }

    private void saveEventRecord(Order order, String eventId, String type, Instant timestamp, Object dto)
            throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(dto);
        EventRecord record = new EventRecord(eventId, type, timestamp == null ? Instant.now() : timestamp, payload);
        record.setOrder(order);
        order.addEventRecord(record);
        eventRecordRepository.save(record);
    }
}
