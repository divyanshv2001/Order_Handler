package com.example.demo;

import com.example.demo.util.EventFileReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class BackendOrderProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendOrderProcessorApplication.class, args);
    }

    @Bean
    public CommandLineRunner runOnStartup(
            @Value("${events.file:classpath:events/sample-events.txt}") String eventsFile,
            @Value("${events.runOnStartup:true}") boolean runOnStartup,
            EventFileReader reader) {
        return args -> {
            if (runOnStartup) {
                System.out.println("Starting event ingestion from: " + eventsFile);
                reader.readAndProcess(eventsFile);
            } else {
                System.out.println("Startup ingestion disabled (events.runOnStartup=false).");
            }
        };
    }
}
