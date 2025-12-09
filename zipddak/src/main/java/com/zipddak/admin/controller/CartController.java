package com.zipddak.admin.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.admin.dto.OrderListToListDto;
import com.zipddak.admin.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CartController {
	
	private final CartService cartService;

	@PostMapping("addCart")
	public ResponseEntity<Boolean> addCart(@RequestBody OrderListToListDto orderListDto){
		
		try {
			// 장바구니에 추가
			cartService.addCart(orderListDto);
			
			return ResponseEntity.ok(true);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	@GetMapping("cartList")
	public ResponseEntity<?> cartList(@RequestParam("username") String username){
		
		try {
			
			
			
			return null;
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
}
