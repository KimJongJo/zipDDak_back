package com.zipddak.admin.service;

import java.util.List;
import java.util.Map;

import com.zipddak.admin.dto.ProductCardDto;
import com.zipddak.admin.dto.ProductDetailResponseDto;
import com.zipddak.util.PageInfo;

public interface ProductService {
	List<ProductCardDto> productList(String keyword, PageInfo pageInfo, Integer sortId, Integer cate1, Integer cate2) throws Exception;

	ProductDetailResponseDto productInfo(Integer productId) throws Exception;
}
