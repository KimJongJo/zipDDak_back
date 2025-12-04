package com.zipddak.admin.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.zipddak.admin.dto.ProductCardDto;
import com.zipddak.admin.repository.ProductDslRepository;
import com.zipddak.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	
	private final ProductDslRepository productDslRepository;

	@Override
	public List<ProductCardDto> productList(String keyword, PageInfo pageInfo, Integer sortId, Integer cate1, Integer cate2) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 16);
		return productDslRepository.productList(keyword, pageRequest, sortId, cate1, cate2);
		
	}

}
