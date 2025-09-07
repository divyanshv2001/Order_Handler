package com.example.demo.util;

import com.example.demo.dto.*;
import com.example.demo.service.EventProcessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;


@Component
public class EventFileReader {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private final EventProcessor processor;

    public EventFileReader(ResourceLoader resourceLoader, ObjectMapper objectMapper, EventProcessor processor) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
        this.processor = processor;
    }

    public void readAndProcess(String resourcePath) {
        try {
            Resource resource = resourceLoader.getResource(resourcePath);
            if (!resource.exists()) {
                System.out.println("Event file not found: " + resourcePath);
                return;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    JsonNode node = objectMapper.readTree(line);
                    String eventType = node.get("eventType").asText();

                    Object dto = switch (eventType) {
                        case "OrderCreated" -> objectMapper.treeToValue(node, OrderCreatedDTO.class);
                        case "PaymentReceived" -> objectMapper.treeToValue(node, PaymentReceivedDTO.class);
                        case "ShippingScheduled" -> objectMapper.treeToValue(node, ShippingScheduledDTO.class);
                        case "OrderCancelled" -> objectMapper.treeToValue(node, OrderCancelledDTO.class);
                        default -> null;
                    };

                    if (dto != null) {
                        // Set timestamp if present
                        if (node.has("timestamp")) {
                            Instant ts = Instant.parse(node.get("timestamp").asText());
                            if (dto instanceof OrderCreatedDTO d) d.setTimestamp(ts);
                            else if (dto instanceof PaymentReceivedDTO d) d.setTimestamp(ts);
                            else if (dto instanceof ShippingScheduledDTO d) d.setTimestamp(ts);
                            else if (dto instanceof OrderCancelledDTO d) d.setTimestamp(ts);
                        }
                        processor.processEvent(dto);
                    } else {
                        System.out.println("Unknown event type: " + eventType);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Error reading events: " + ex.getMessage());
        }
    }
}
