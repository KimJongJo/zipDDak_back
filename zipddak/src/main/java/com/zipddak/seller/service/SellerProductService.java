package com.zipddak.seller.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.zipddak.dto.ProductDto;
import com.zipddak.entity.Category;
import com.zipddak.seller.dto.CategoryResponseDto;
import com.zipddak.seller.dto.SaveResultDto;

public interface SellerProductService {

	List<CategoryResponseDto> getCategoryTree() throws Exception; //카테고리 리스트 조회
	SaveResultDto productRegist(ProductDto product_dto, MultipartFile thumbnail, MultipartFile[] addImageFiles, MultipartFile[] detailImageFiles, String optionsJson) throws Exception;
	

}
