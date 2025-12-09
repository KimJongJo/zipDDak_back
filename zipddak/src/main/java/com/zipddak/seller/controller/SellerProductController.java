package com.zipddak.seller.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zipddak.dto.CategoryDto;
import com.zipddak.dto.ProductDto;
import com.zipddak.seller.dto.CategoryResponseDto;
import com.zipddak.seller.dto.SaveResultDto;
import com.zipddak.seller.service.SellerProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/seller/product")
@RequiredArgsConstructor
public class SellerProductController {
	
	private final SellerProductService product_svc;
	
	//카테고리 리스트 조회
	@GetMapping("/categories/all")
    public List<CategoryResponseDto> getCategories() {
        try {
			return product_svc.getCategoryTree();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
	
	
	//상품 등록
	@PostMapping("/regist")
    public ResponseEntity<?> productRegist(ProductDto product_dto, 
			    							@RequestParam(value="thumbnailFile") MultipartFile thumbnail, 
											@RequestParam(value="addImageFiles", required=false) MultipartFile[] addImageFiles, 
											@RequestParam(value="detailImageFiles") MultipartFile[] detailImageFiles,
											@RequestParam(value = "options", required = false) String optionsJson) {
		System.out.println("pDto : " + product_dto);
		System.out.println(optionsJson);
		
        try {
        	product_dto.setSellerUsername("test");
        	SaveResultDto result = product_svc.productRegist(product_dto, thumbnail, addImageFiles, detailImageFiles, optionsJson);

            if (!result.isSuccess()) { //상품 등록 실패한 경우 
                return ResponseEntity.badRequest().body(result);
            }

            return ResponseEntity.ok(result);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
    }
	
	
	//리스트 출력 
	//셀러가 등록한 상품의 카테고리만 필터박스에 세팅 
	@GetMapping("/categories")
	public ResponseEntity<?> getSellerCategories(@RequestParam("sellerId") String sellerUsername) {
		try {
			List<CategoryDto> sellerCategories = product_svc.getSellerCategories(sellerUsername);
			return ResponseEntity.ok(sellerCategories);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	//상품 리스트
	@GetMapping("/myProductList")
	public ResponseEntity<?> getProductList(@RequestParam("sellerId") String sellerUsername,
											@RequestParam(value="visible", required = false) String visible,
								            @RequestParam(value="category", required = false) String category,
								            @RequestParam(value="keyword", required = false) String keyword,
								            @RequestParam(value="page", required=false, defaultValue="1") Integer page) {
		System.out.println("sellerUsername : " + sellerUsername);
		
		try {
			Map<String, Object> sellerProductList = product_svc.searchMyProductList(sellerUsername, visible, category, keyword, page);
			return ResponseEntity.ok(sellerProductList);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
        
    }
	
	

}
