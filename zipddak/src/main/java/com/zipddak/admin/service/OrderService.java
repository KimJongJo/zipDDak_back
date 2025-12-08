package com.zipddak.admin.service;

import java.util.List;

import com.zipddak.admin.dto.OrderListDto;
import com.zipddak.admin.dto.RecvUserDto;
import com.zipddak.dto.OrderDto;

public interface OrderService {

	void addOrder(String username, String orderId, Integer amount, Integer postCharge, RecvUserDto recvUser,
			List<OrderListDto> orderList, Integer productId);

	OrderDto getOrderInfo(String orderCode) throws Exception;

}
