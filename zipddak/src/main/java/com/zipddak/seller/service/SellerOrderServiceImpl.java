package com.zipddak.seller.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.zipddak.dto.OrderDto;
import com.zipddak.dto.OrderItemDto;
import com.zipddak.entity.Order;
import com.zipddak.repository.OrderRepository;
import com.zipddak.seller.dto.SearchConditionDto;
import com.zipddak.seller.repository.SellerOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerOrderServiceImpl implements SellerOrderService {

	private final OrderRepository order_repo;
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

	@Override
	public Map<String, Object> getMyOrderDetail(String sellerUsername, Integer orderIdx) throws Exception {
		 // 셀러 소유 OrderItem만 가져오기
	    List<OrderItemDto> itemList = sellerOrder_repo.findMyOrderItems(sellerUsername, orderIdx);

	    if (itemList.isEmpty()) {
	        throw new Exception("해당 주문은 이 셀러의 상품이 아님");
	    }

	    // orderIdx 하나니까 orders에서 order 정보 하나 가져오기
	    Order order = order_repo.findById(orderIdx).orElseThrow(() -> new Exception("주문 없음"));

	    Map<String, Object> result = new HashMap<>();
	    result.put("orderData", order.toDto());
	    result.put("myOrderItemList", itemList);

	    return result;
	}


}
