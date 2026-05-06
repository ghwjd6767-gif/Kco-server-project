package com.sparta.kcoserverproject.domain.product.repository;

import com.sparta.kcoserverproject.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
