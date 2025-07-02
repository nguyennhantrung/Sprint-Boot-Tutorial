package com.tutorial.apidemo.database;


import com.tutorial.apidemo.models.Product;
import com.tutorial.apidemo.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    // Logger
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return new  CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Product productA = new Product("Ipad pro", 2018, 1000.0, "");
                Product productB = new Product("Ipad pro", 2020, 1200.0, "");
                logger.info("Insert Data: {}", productRepository.save(productA));
                logger.info("Insert Data: {}", productRepository.save(productB));
            };
        };
    }
}
