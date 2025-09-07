package com.example.demo.observer;

import com.example.demo.entity.Order;

public interface OrderObserver {
    void onEventProcessed(Order order, Object event);
}
