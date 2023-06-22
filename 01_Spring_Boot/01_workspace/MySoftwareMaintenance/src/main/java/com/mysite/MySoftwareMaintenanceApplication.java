package com.mysite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MySoftwareMaintenanceApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MySoftwareMaintenanceApplication.class, args);
    }
}
