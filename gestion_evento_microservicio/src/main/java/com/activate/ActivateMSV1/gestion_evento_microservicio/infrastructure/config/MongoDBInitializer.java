package com.activate.ActivateMSV1.gestion_evento_microservicio.infrastructure.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoDBInitializer {

    @Bean
    CommandLineRunner init(MongoTemplate mongoTemplate) {
        return args -> {
        };
    }
}