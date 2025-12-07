package com.zipddak.seller.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zipddak.dto.ProductDto;
import com.zipddak.seller.dto.CategoryResponseDto;
import com.zipddak.seller.dto.SaveResultDto;
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
	
	
	//상품 등록
	@PostMapping("/regist")
    public ResponseEntity<?> productRegist(ProductDto product_dto, 
			    							@RequestParam(value="thumbnailFile") MultipartFile thumbnail, 
											@RequestParam(value="addImageFiles", required=false) MultipartFile[] addImageFiles, 
											@RequestParam(value="detailImageFiles") MultipartFile[] detailImageFiles,
											@RequestParam(value = "options", required = false) String optionsJson) {
//		System.out.println("pDto : " + product_dto);
		
        try {
        	product_dto.setSellerUsername("test");
        	SaveResultDto result = sellerPd_svc.productRegist(product_dto, thumbnail, addImageFiles, detailImageFiles, optionsJson);

            if (!result.isSuccess()) { //상품 등록 실패한 경우 
                return ResponseEntity.badRequest().body(result);
            }

            return ResponseEntity.ok(result);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
    }
	
	

}
