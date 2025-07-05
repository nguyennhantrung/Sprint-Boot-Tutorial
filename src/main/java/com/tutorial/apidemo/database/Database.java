package com.tutorial.apidemo.database;


import com.tutorial.apidemo.models.Product;
import com.tutorial.apidemo.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// now connect to mysql using jpa
/*
docker run -d --rm --name mysql-spring-boot-tutorial \
-e MYSQL_ROOT_PASSWORD=123456 \
-e MYSQL_USER=nntrung \
-e MYSQL_PASSWORD=123456 \
-e MYSQL_DATABASE=test_db \
-p 3309:3306 \
--volume mysql-spring-boot-tutorial-volume:/var/lib/mysql \
mysql:latest

docker ps
mysql -h localhost -P 3309 --protocol=tcp -u nntrung -p

show databases
use test_db;
show tables;
 */

@Configuration
public class Database {
    // Logger
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return new  CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                Product productA = new Product("Ipad pro", 2018, 1000.0, "");
//                Product productB = new Product("Ipad mini", 2020, 1200.0, "");
//                logger.info("Insert Data: {}", productRepository.save(productA));
//                logger.info("Insert Data: {}", productRepository.save(productB));
            };
        };
    }
}
