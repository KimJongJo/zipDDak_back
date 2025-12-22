package com.zipddak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	List<OrderItem> findByOrderIdx(Integer orderIdx);
	
	//SELECT order_item_idx FROM order_item WHERE order_idx = ? and order_item_idx = ?
	List<OrderItem> findOrderItemIdxByOrderIdxAndOrderItemIdxIn(Integer orderIdx, List<Integer> itemIdxs); //특정 orderIdx에 포함된 orderItemIdx리스트 select

	List<OrderItem> findByRefundIdx(Integer refundIdx);
}
