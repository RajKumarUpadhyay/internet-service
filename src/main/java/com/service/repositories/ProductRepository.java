package com.service.repositories;

import com.service.entities.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findAll();
    Product findProductByProductType(String product_type);
}
