package com.example.demo.observer;

import com.example.demo.entity.Order;
import org.springframework.stereotype.Component;

/**
 * Observer that logs events and status changes.
 */
@Component
public class LoggerObserver implements OrderObserver {

    @Override
    public void onEventProcessed(Order order, Object event) {
        System.out.println("[LoggerObserver] Event processed: " +
                (event != null ? event.getClass().getSimpleName() : "null") +
                " for order " + (order != null ? order.getOrderId() : "n/a") +
                " with status " + (order != null ? order.getStatus() : "n/a"));
    }
}
