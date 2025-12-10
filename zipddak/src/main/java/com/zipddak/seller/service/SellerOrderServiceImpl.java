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
	public Map<String, Object> getMyOrderList(String sellerUsername, Integer page,  SearchConditionDto scDto) throws Exception {
		PageRequest pr = PageRequest.of(page - 1, 10);
		
		List<OrderDto> myOrderList = sellerOrder_repo.searchMyOrders(sellerUsername, pr, scDto);  //주문리스트
		Long myOrderCount = sellerOrder_repo.countMyOrders(sellerUsername, scDto);	//주문서 개수 

        int allPage = (int) Math.ceil(myOrderCount / 10.0);
        int startPage = (page - 1) / 10 * 10 + 1;
        int endPage = Math.min(startPage + 9, allPage);

        Map<String, Object> result = new HashMap<>();
        result.put("curPage", page);
        result.put("allPage", allPage);
        result.put("startPage", startPage);
        result.put("endPage", endPage);
        result.put("myOrderList", myOrderList);
        result.put("myOrderCount", myOrderCount);
        
		return result;
	}


}
