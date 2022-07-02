package com.triple.mileage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MileageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MileageApplication.class, args);
    }

}
