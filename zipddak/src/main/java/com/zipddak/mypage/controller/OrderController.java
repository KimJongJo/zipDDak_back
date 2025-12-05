package com.zipddak.mypage.controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.mypage.dto.OrderListDto;
import com.zipddak.mypage.service.OrderServiceImpl;
import com.zipddak.util.PageInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderController {
	private final OrderServiceImpl orderService;

	// 주문배송목록 조회
	@GetMapping("/market/orderList")
	public ResponseEntity<Map<String, Object>> orderList(@RequestParam("username") String username,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "startDate", required = false) Date startDate,
			@RequestParam(value = "endDate", required = false) Date endDate) {
		try {
			PageInfo pageInfo = new PageInfo(page);

			List<OrderListDto> orderListDtoList = orderService.getOrderList(username, pageInfo, startDate, endDate);

			Map<String, Object> res = new HashMap<>();
			res.put("orderListDtoList", orderListDtoList);
			res.put("pageInfo", pageInfo);

			return ResponseEntity.ok(res);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}
}
