package com.tutorial.apidemo.models;


import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Objects;

// POJO: Plain Object Java Object
@Entity
@Table(name="tblProduct")
public class Product {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    // you also can use sequence
    @SequenceGenerator(
            name="product_sequence",
            sequenceName = "product_sequence",
            allocationSize=1 // increase by 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long id;
    // Fix Column character: no null table, all data are unique
    @Column(nullable = false, unique = true, length = 300)
    private String productName;
    private int productionYear;
    private Double price;
    private String url;
    // default constructor
    public Product() {}

    //Calculated field = transient (not exist in MySQl)
    @Transient
    private int age;
    public int getAge() {
        return Calendar.getInstance().get(Calendar.YEAR) - productionYear;
    }

    public Product(String productName, int productionYear, Double price, String url) {
        this.productName = productName;
        this.productionYear = productionYear;
        this.price = price;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getproductionYear() {
        return productionYear;
    }

    public void setproductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productionYear=" + productionYear +
                ", price=" + price +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productionYear == product.productionYear &&
                Objects.equals(productName, product.productName) &&
                Objects.equals(price, product.price) &&
                Objects.equals(url, product.url);
    }
}
