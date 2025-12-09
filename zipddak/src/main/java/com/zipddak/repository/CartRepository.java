package com.zipddak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer>{

}
