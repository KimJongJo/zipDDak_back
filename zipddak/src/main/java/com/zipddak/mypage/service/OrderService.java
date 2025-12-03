package com.zipddak.mypage.service;

import java.util.List;

import com.zipddak.mypage.dto.OrderListDto;

public interface OrderService {
	List<OrderListDto> getOrderList(String username) throws Exception;
}
