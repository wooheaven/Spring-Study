package com.mysite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MySoftwareMaintenanceApplication {

    public static void main(String[] args) throws Exception {
        Tmp tmp = new Tmp();
        tmp.db();
        SpringApplication.run(MySoftwareMaintenanceApplication.class, args);
    }
}
