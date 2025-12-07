package com.zipddak.seller.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponseDto {
	
	private Integer categoryIdx;
	private String name;
	private List<SubCategoryResponseDto> subCategories;

}
