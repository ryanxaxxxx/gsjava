package com.greenway.greenway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GreenWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenWayApplication.class, args);
    }
}
