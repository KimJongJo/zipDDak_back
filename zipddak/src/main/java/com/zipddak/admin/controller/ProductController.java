package com.zipddak.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.admin.service.ProductService;
import com.zipddak.dto.ProductDto;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	

	// 자재 리스트 조회
	@GetMapping("productList")
	public ResponseEntity<List<ProductDto>> productList(
			@RequestParam(required = false, defaultValue = "1") Integer sortId,
			@RequestParam("cate1") Integer cate1,
			@RequestParam(required = false) Integer cate2) {
		
		try {
				List<ProductDto> productList = productService.productList(sortId, cate1, cate2);
				
				return ResponseEntity.ok(productList);

		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
}
