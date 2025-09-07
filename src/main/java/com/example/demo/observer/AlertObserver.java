package com.example.demo.observer;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderStatus;
import org.springframework.stereotype.Component;


@Component
public class AlertObserver implements OrderObserver {

    @Override
    public void onEventProcessed(Order order, Object event) {
        if (order == null) return;

        if (order.getStatus() == OrderStatus.CANCELLED ||
            order.getStatus() == OrderStatus.SHIPPED) {
            System.out.println("[AlertObserver] ALERT: Order " +
                    order.getOrderId() + " changed to " + order.getStatus());
        }
    }
}
