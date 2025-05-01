package com.fynaloo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FynalooApplication {

    public static void main(String[] args) {
        SpringApplication.run(FynalooApplication.class, args);
    }

}
