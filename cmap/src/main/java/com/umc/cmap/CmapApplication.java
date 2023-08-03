package com.umc.cmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CmapApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmapApplication.class, args);
    }
}