package com.zipddak.admin.service;

import java.util.List;
import java.util.Map;

import com.zipddak.admin.dto.ProductCardDto;
import com.zipddak.admin.dto.ProductDetailResponseDto;
import com.zipddak.admin.dto.ProductInquiriesDto;
import com.zipddak.admin.dto.ProductReviewsDto;
import com.zipddak.util.PageInfo;

public interface ProductService {
	List<ProductCardDto> productList(String keyword, PageInfo pageInfo, Integer sortId, Integer cate1, Integer cate2) throws Exception;

	ProductDetailResponseDto productInfo(Integer productId) throws Exception;

	List<ProductReviewsDto> moreReview(Integer productId, Integer page) throws Exception;

	List<ProductInquiriesDto> moreInquiry(Integer productId, Integer page) throws Exception;
}
