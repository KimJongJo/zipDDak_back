package com.zipddak.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.admin.dto.ProductCardDto;
import com.zipddak.admin.dto.ProductDetailResponseDto;
import com.zipddak.admin.service.ProductService;
import com.zipddak.util.PageInfo;

import lombok.RequiredArgsConstructor;




@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;

	// 자재 리스트 조회
	@GetMapping("productList")
	public ResponseEntity<List<ProductCardDto>> productList(
			@RequestParam(required = false) String keyword,
			@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "1") Integer sortId,
			@RequestParam("cate1") Integer cate1,
			@RequestParam(required = false) Integer cate2) {
		
		try {
				PageInfo pageInfo = new PageInfo(page);
				List<ProductCardDto> productList = productService.productList(keyword, pageInfo, sortId, cate1, cate2);
				
				return ResponseEntity.ok(productList);

		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	// 상품 상세 페이지
	@GetMapping("product")
	public ResponseEntity<ProductDetailResponseDto> productInfo(@RequestParam("productId") Integer productId){
		
		try {
			ProductDetailResponseDto productInfo = productService.productInfo(productId);
			
			System.out.println(productInfo);
			
			return ResponseEntity.ok(productInfo);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	
	
}
