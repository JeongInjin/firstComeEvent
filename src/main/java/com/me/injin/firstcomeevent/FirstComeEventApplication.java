package com.me.injin.firstcomeevent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FirstComeEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirstComeEventApplication.class, args);
    }

}
