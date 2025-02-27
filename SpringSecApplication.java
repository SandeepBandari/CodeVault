package com.example.Spring_Sec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecApplication {

    private static final Logger logger = LoggerFactory.getLogger(SpringSecApplication.class);

 
    public static void main(String[] args) {
        SpringApplication.run(SpringSecApplication.class, args);
        logger.info("Application started successfully!"); // Simple logging example
    }
}