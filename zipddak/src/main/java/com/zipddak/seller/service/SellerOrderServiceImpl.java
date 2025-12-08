package com.zipddak.seller.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.zipddak.dto.OrderDto;
import com.zipddak.dto.ProductDto;
import com.zipddak.seller.dto.SearchConditionDto;
import com.zipddak.seller.repository.SellerOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerOrderServiceImpl implements SellerOrderService {

	private final SellerOrderRepository sellerOrder_repo;

	private final ModelMapper model_mapper;

	@Override
	public List<OrderDto> getMyOrderList(PageRequest pr, SearchConditionDto scDto) throws Exception {
		return sellerOrder_repo.searchMyOrders(pr, scDto);
	}

	@Override
	public Long getMyOrderCount(SearchConditionDto scDto) throws Exception {
		return sellerOrder_repo.countMyOrders(scDto);
	}

}
