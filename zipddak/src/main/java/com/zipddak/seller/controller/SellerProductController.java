package com.zipddak.seller.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.seller.dto.CategoryResponseDto;
import com.zipddak.seller.service.SellerProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/seller/product")
@RequiredArgsConstructor
public class SellerProductController {
	
	private final SellerProductService sellerPd_svc;
	
	//카테고리 리스트 조회
	@GetMapping("/categories")
    public List<CategoryResponseDto> getCategories() {
        try {
        	
			return sellerPd_svc.getCategoryTree();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
	
	

}
