package com.zipddak.admin.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.zipddak.admin.dto.ProductCardDto;
import com.zipddak.admin.dto.ProductDetailDto;
import com.zipddak.admin.dto.ProductDetailResponseDto;
import com.zipddak.admin.dto.ProductImagesDto;
import com.zipddak.admin.dto.ProductInquiriesDto;
import com.zipddak.admin.dto.ProductReviewsDto;
import com.zipddak.admin.repository.ProductDslRepository;
import com.zipddak.dto.ProductDto;
import com.zipddak.repository.ProductRepository;
import com.zipddak.repository.ReviewProductRepository;
import com.zipddak.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	
	private final ProductDslRepository productDslRepository;
	
	private final ReviewProductRepository reviewProductRepository;

	@Override
	public List<ProductCardDto> productList(String keyword, PageInfo pageInfo, Integer sortId, Integer cate1, Integer cate2) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 16);
		return productDslRepository.productList(keyword, pageRequest, sortId, cate1, cate2);
		
	}

	// 상품 정보들을 포함한 map 반환
	@Override
	public ProductDetailResponseDto productInfo(Integer productId) throws Exception {

		// 상품 정보
		ProductDetailDto productDetailDto = productDslRepository.productInfo(productId);
		
		
		// 상품 이미지 리스트
//		List<ProductImagesDto> productImages = productDslRepository.productImages(productId);
		
		
		// 처음 상품 디테일 페이지에서 리뷰, 문의를 각각 5개씩 불러옴
		PageInfo pageInfo = new PageInfo(1); // 처음 페이지 1로 고정
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 5); // 5개씩
		
		
		// 상품 리뷰 리스트
//		List<ProductReviewsDto> productReviews = productDslRepository.productReviews(productId, pageRequest);


		// 상품 문의 리스트		
		List<ProductInquiriesDto> productInquiries = productDslRepository.productInquiries(productId, pageRequest);
		

		// 평점
		Double avgScore = reviewProductRepository.findAvgByProductIdx(productId);
		
		
		// 리뷰 수
		Long reviewCount = reviewProductRepository.countByProductIdx(productId);

		
		
		// DTO 조립 후 반환
		return ProductDetailResponseDto.builder()
				.productDetailDto(productDetailDto)
//				.productImages(productImages)
//				.productReviews(productReviews)
				.productInquiries(productInquiries)
				.avgScore(avgScore)
				.reviewCount(reviewCount)
				.build();
		
	}


}
