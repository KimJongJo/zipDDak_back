package com.zipddak.seller.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;

import com.zipddak.dto.OrderDto;
import com.zipddak.seller.dto.SearchConditionDto;

public interface SellerOrderService {

	List<OrderDto> getMyOrderList(PageRequest pr, SearchConditionDto scDto) throws Exception;
    Long getMyOrderCount(SearchConditionDto scDto) throws Exception;

}
