package com.zipddak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
