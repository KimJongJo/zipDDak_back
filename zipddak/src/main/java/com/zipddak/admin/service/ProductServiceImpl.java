package com.zipddak.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zipddak.admin.repository.ProductDslRepository;
import com.zipddak.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	
	private final ProductDslRepository productDslRepository;

	@Override
	public List<ProductDto> productList(Integer sortId, Integer cate1, Integer cate2) throws Exception {
		return productDslRepository.productList(sortId, cate1, cate2);
		
	}

}
