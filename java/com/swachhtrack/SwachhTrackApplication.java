package com.swachhtrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SwachhTrackApplication {
    public static void main(String[] args) {
        SpringApplication.run(SwachhTrackApplication.class, args);
        System.out.println("Clean city");
    }
}
