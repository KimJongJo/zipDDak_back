package com.zipddak.admin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.zipddak.admin.dto.ColorOption;
import com.zipddak.admin.dto.ProductCardDto;
import com.zipddak.admin.dto.ProductDetailDto;
import com.zipddak.admin.dto.ProductDetailResponseDto;
import com.zipddak.admin.dto.ProductImagesDto;
import com.zipddak.admin.dto.ProductInquiriesDto;
import com.zipddak.admin.dto.ProductReviewsDto;
import com.zipddak.admin.repository.ProductDslRepository;
import com.zipddak.dto.ProductDto;
import com.zipddak.dto.ProductOptionDto;
import com.zipddak.repository.InquiriesRepository;
import com.zipddak.repository.ProductOptionRepository;
import com.zipddak.repository.ProductRepository;
import com.zipddak.repository.ReviewProductRepository;
import com.zipddak.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	
	private final ProductDslRepository productDslRepository;
	
	private final ReviewProductRepository reviewProductRepository;
	private final ProductOptionRepository productOptionRepository;
	private final InquiriesRepository inquiriesRepository;

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
		List<ProductReviewsDto> productReviews = productDslRepository.productReviews(productId, pageRequest);


		// 상품 문의 리스트		
		List<ProductInquiriesDto> productInquiries = productDslRepository.productInquiries(productId, pageRequest);
		

		// 평점
		Double avgScore = reviewProductRepository.findAvgByProductIdx(productId);
		
		
		// 리뷰 수
		Long reviewCount = reviewProductRepository.countByProductIdx(productId);
		
		// 문의 수
		Long inquiryCount = inquiriesRepository.countByProductIdxAndAnswerIsNotNull(productId);
		
		// 상품 옵션
		List<ProductOptionDto> productOptions = productOptionRepository.findByProduct_ProductIdx(productId).stream().map(option -> option.toProductOptionDto()).collect(Collectors.toList());
		
		Map<String, List<ColorOption>> productOption = new HashMap<String, List<ColorOption>>();
		for(ProductOptionDto option : productOptions) {
			// 옵션명
			String name = option.getName();
			// 값 -> 색상
			String color = option.getValue();
			Long price = option.getPrice();
			Integer optionId = option.getProductOptionIdx();
			
			// 옵션명을 key로 색상리스트를 value로 저장하는 map
			// computeIfAbsent -> key가 map에 없으면 새로운 arrayList를 만들어서 value를 넣고 있으면 기존 리스트를 가져옴
			productOption.computeIfAbsent(name, k -> new ArrayList<>())
				.add(new ColorOption(optionId, price, color));
		}

		// DTO 조립 후 반환
		return ProductDetailResponseDto.builder()
				.productDetailDto(productDetailDto)
				.productReviews(productReviews)
				.productInquiries(productInquiries)
				.avgScore(avgScore)
				.reviewCount(reviewCount)
				.productOption(productOption)
				.inquiryCount(inquiryCount)
				.build();
		
	}

	// 추가 리뷰
	@Override
	public List<ProductReviewsDto> moreReview(Integer productId, Integer page) throws Exception {
		
		PageInfo pageInfo = new PageInfo(page);
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 5); // 5개씩
		
		return productDslRepository.productReviews(productId, pageRequest);
	}

	@Override
	public List<ProductInquiriesDto> moreInquiry(Integer productId, Integer page) throws Exception {
		
		PageInfo pageInfo = new PageInfo(page);
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 5); // 5개씩
		
		return productDslRepository.productInquiries(productId, pageRequest);
	}


}
