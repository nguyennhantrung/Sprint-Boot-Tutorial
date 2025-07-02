package com.tutorial.apidemo.controllers;

import java.util.List;
import java.util.Optional;

import com.tutorial.apidemo.models.Product;
import com.tutorial.apidemo.models.ResponseObject;
import com.tutorial.apidemo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/Products")
public class ProductController {
    // DI: Dependency Injection (same as singleton)
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("") // http://localhost:8080/api/v1/Products
    List<Product> getAllProducts() {
//        return List.of(
//            new Product(1L, "Ipad pro", 2018, 1000.0, ""),
//            new Product(1L, "Ipad pro", 2020, 1200.0, "")
//        );
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getProductById(@PathVariable Long id) {
        Optional<Product> foundProduct = productRepository.findById(id);
        return foundProduct.isPresent() ?
            ResponseEntity.status(HttpStatus.OK).body(
                new  ResponseObject("OK", "Query product successfully", foundProduct.get())
            ):
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new  ResponseObject("ERROR", "Product not found", "")
            );
    }
}
