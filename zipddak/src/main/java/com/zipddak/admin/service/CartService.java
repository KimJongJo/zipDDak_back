package com.zipddak.admin.service;

import com.zipddak.admin.dto.OrderListToListDto;

public interface CartService {

	void addCart(OrderListToListDto orderListDto) throws Exception;

}
