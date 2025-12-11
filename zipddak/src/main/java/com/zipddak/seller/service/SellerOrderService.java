package com.zipddak.seller.service;

import java.util.Map;

import com.zipddak.dto.OrderDto;
import com.zipddak.seller.dto.SearchConditionDto;

public interface SellerOrderService {

	Map<String, Object> getMyOrderList(String sellerUsername, Integer page, SearchConditionDto scDto) throws Exception;

	 Map<String, Object> getMyOrderDetail(String sellerUsername, Integer orderIdx) throws Exception;

}
