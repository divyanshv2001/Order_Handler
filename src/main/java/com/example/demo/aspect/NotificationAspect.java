package com.example.demo.aspect;

import com.example.demo.annotation.NotifyObservers;
import com.example.demo.entity.Order;
import com.example.demo.observer.OrderObserver;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;


@Aspect
@Component
public class NotificationAspect {

    private final List<OrderObserver> observers;

    public NotificationAspect(List<OrderObserver> observers) {
        this.observers = observers;
    }

    @AfterReturning(pointcut = "@annotation(com.example.demo.aspect.annotation.NotifyObservers)",
                    returning = "returned")
    public void afterEventProcessed(JoinPoint joinPoint, Object returned) {
        Object[] args = joinPoint.getArgs();
        Object dto = args.length > 0 ? args[0] : null;

        Order order = null;
        if (returned instanceof Order) {
            order = (Order) returned;
        }

        for (OrderObserver observer : observers) {
            try {
                observer.onEventProcessed(order, dto);
            } catch (Exception ex) {
                System.out.println("Observer error: " + ex.getMessage());
            }
        }
    }
}
