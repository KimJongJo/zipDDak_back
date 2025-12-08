package com.zipddak.admin.service;

import java.util.List;

import com.zipddak.admin.dto.OrderListDto;
import com.zipddak.admin.dto.PaymentComplateDto;

public interface PaymentService {


	Integer getTotalPrice(Integer productId, List<OrderListDto> orderList, Integer postCharge);

	String getOrderName(Integer productId, List<OrderListDto> orderList);

	void approvePayment(PaymentComplateDto paymentComplateDto) throws Exception;

}
