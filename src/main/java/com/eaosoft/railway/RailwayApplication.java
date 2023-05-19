package com.eaosoft.railway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.eaosoft.mqtt")
@ComponentScan("com.eaosoft.railway")
public class RailwayApplication {
    public static void main(String[] args) {
        SpringApplication.run(RailwayApplication.class, args);
    }

}
