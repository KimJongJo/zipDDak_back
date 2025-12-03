package com.zipddak.mypage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zipddak.mypage.dto.OrderListDto;
import com.zipddak.repository.OrderItemRepository;
import com.zipddak.repository.OrderRepository;
import com.zipddak.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private OrderRepository orderRepository;
	private OrderItemRepository orderItemRepository;
	private ProductRepository productRepository;

	// 주문배송목록 가져오기
	@Override
	public List<OrderListDto> getOrderList(String username) throws Exception {

		return null;
	}

}
