package com.walmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController

public class AssignmentApplication {

  
    public static void main(String[] args) {
        SpringApplication.run(AssignmentApplication.class, args);
    }
    
    
    @RequestMapping(value="/test")
    public String hello(){
        return "Hello test FEED";
    }
   
   
    
}
