package com.zawadzkidevelop.b2borderservice.model.repository;

import com.zawadzkidevelop.b2borderservice.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
