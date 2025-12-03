package com.zipddak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
