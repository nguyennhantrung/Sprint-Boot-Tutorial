package com.tutorial.apidemo.controllers;

import java.util.List;
import java.util.Optional;

import com.tutorial.apidemo.models.Product;
import com.tutorial.apidemo.models.ResponseObject;
import com.tutorial.apidemo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/Products")
public class ProductController {
    // DI: Dependency Injection (same as singleton)
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("") // http://localhost:8080/api/v1/Products
    List<Product> getAllProducts() {
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

    // Insert new product with POST method
    // Postman: Raw, JSON
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct) {
        // 2 products must not have the same name
        List<Product> foundProducts = productRepository.findByProductName(newProduct.getProductName().trim());
        if(foundProducts.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new  ResponseObject("ERROR", "Product already exists", "")
            );
        }
        return  ResponseEntity.status(HttpStatus.OK).body(
                new   ResponseObject("OK", "Insert product successfully", productRepository.save(newProduct))
        );
    }

    // update, upsert = update if found, otherwise insert
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        Product foundProduct = productRepository.findById(id)
                .map(product -> {
                    product.setProductName(newProduct.getProductName().trim());
                    product.setPrice(newProduct.getPrice());
                    product.setproductionYear(newProduct.getproductionYear());
                    return  productRepository.save(product);
                }).orElseGet(()-> {
//                    newProduct.setId(id);
                    return productRepository.save(newProduct);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new  ResponseObject("OK", "Update product successfully", foundProduct)
        );
    }

    // delete a product by id
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        boolean canDelete = productRepository.existsById(id);
        if(canDelete) {
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new  ResponseObject("OK", "Delete product successfully", productRepository.findById(id))
            );
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new  ResponseObject("ERROR", "Product not found", "")
            );
        }
    }
}
