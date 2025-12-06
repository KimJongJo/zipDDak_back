package com.zipddak.mypage.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zipddak.dto.OrderDto;
import com.zipddak.mypage.dto.ReturnRequestDto;
import com.zipddak.mypage.service.OrderClaimServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderClaimController {

	private final OrderClaimServiceImpl orderClaimService;

	// 주문 취소
	@PostMapping("/market/cancel")
	public ResponseEntity<Boolean> orderCancel(@RequestBody List<Integer> orderItemIdxs) {
		try {
			orderClaimService.cancelOrderItem(orderItemIdxs);
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}

	// 주문 조회
	@GetMapping("/market/orderInfo")
	public ResponseEntity<OrderDto> orderInfo(@RequestParam("orderIdx") Integer orderIdx) {
		try {
			OrderDto orrderDto = orderClaimService.getOrderInfo(orderIdx);

			return ResponseEntity.ok(orrderDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}

	// 반품 신청
	@PostMapping("/market/return")
	public ResponseEntity<Boolean> orderReturn(ReturnRequestDto returnRequest,
			@RequestParam(value = "returnImages", required = false) MultipartFile[] returnImages) {
		try {
			orderClaimService.returnOrderItem(returnRequest, returnImages);
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}
}
