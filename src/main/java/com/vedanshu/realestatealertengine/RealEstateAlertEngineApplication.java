package com.vedanshu.realestatealertengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RealEstateAlertEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(RealEstateAlertEngineApplication.class, args);
    }
}
