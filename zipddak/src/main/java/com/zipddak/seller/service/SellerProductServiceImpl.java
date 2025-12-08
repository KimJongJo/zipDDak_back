package com.zipddak.seller.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zipddak.entity.Category;
import com.zipddak.repository.CategoryRepository;
import com.zipddak.seller.dto.CategoryResponseDto;
import com.zipddak.seller.dto.SubCategoryResponseDto;
import com.zipddak.seller.repository.SellerProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerProductServiceImpl implements SellerProductService {
	
	private final CategoryRepository category_repo;
//	private final SellerProductRepository sellerPd_repo;
	
	//카테고리 리스트 조회
	public List<CategoryResponseDto> getCategoryTree() throws Exception {

	    // 대카테고리 : depth = 1 + type = "product" 
	    List<Category> pdCateList = category_repo.findByDepthAndType(1, "product");

	    List<CategoryResponseDto> result = new ArrayList<>();
	    for (Category ct : pdCateList) {

	        // 소카테고리 : depth = 2 + type = "product" 
	        List<Category> children = category_repo.findByParentIdxAndType(ct.getCategoryIdx(), "product");

	        CategoryResponseDto CateResDto = new CategoryResponseDto();
	        CateResDto.setCategoryIdx(ct.getCategoryIdx());
	        CateResDto.setName(ct.getName());

	        // 소카테 DTO 변환
	        List<SubCategoryResponseDto> subList 
	        	= children.stream().map(c -> {
												SubCategoryResponseDto sc = new SubCategoryResponseDto();
							                    sc.setCategoryIdx(c.getCategoryIdx());
							                    sc.setName(c.getName());
							                    return sc;
							                }).collect(Collectors.toList());

	        CateResDto.setSubCategories(subList);

	        result.add(CateResDto);
	    }
	    return result;
	}

}
