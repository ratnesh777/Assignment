package com.walmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = { "com.walmart" } ) 
public class AssignmentApplicationTests {
    public static void main(String[] args) {
        SpringApplication.run(AssignmentApplicationTests.class, args);
    }

}