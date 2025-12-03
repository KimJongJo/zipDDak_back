package com.zipddak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	
}
