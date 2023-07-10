package com.mysite.brew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BrewMaintenanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrewMaintenanceApplication.class, args);
    }

}
