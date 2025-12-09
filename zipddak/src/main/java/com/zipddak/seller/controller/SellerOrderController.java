package com.zipddak.seller.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.dto.OrderDto;
import com.zipddak.seller.dto.SearchConditionDto;
import com.zipddak.seller.service.SellerOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/seller/order")
@RequiredArgsConstructor
public class SellerOrderController {

	private final SellerOrderService order_svc;

	// 주문 리스트
	@GetMapping("/myOrderList")
	public ResponseEntity<?> orderList(@RequestParam("sellerId") String sellerUsername, @RequestParam int page, @RequestParam int size, SearchConditionDto scDto) {
		PageRequest pr = PageRequest.of(page, size);

		List<OrderDto> myOrderList;
		try {
			myOrderList = order_svc.getMyOrderList(pr, scDto);
			Long myOrderCount = order_svc.getMyOrderCount(scDto);

			Map<String, Object> result = new HashMap<>();
			result.put("myOrderList", myOrderList);
			result.put("myOrderCount", myOrderCount);
			
			return ResponseEntity.ok(result);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}
