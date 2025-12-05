package com.zipddak.admin.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductDetailResponseDto {
	
	ProductDetailDto productDetailDto; // 상품 정보
	List<ProductImagesDto> productImages; // 상품 이미지
	List<ProductReviewsDto> productReviews; // 상품 리뷰
	List<ProductInquiriesDto> productInquiries; // 상품 문의
	Double avgScore; // 상품 평점
	Long reviewCount; // 리뷰 수
	
	
}
