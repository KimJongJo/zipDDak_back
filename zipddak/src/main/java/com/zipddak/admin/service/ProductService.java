package com.zipddak.admin.service;

import java.util.List;

import com.zipddak.dto.ProductDto;

public interface ProductService {
	List<ProductDto> productList(Integer sortId, Integer cate1, Integer cate2) throws Exception;
}
