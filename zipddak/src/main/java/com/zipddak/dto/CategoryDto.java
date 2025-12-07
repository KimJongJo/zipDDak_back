package com.zipddak.dto;

import com.zipddak.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
	private Integer categoryIdx;
	private String name;
	private Integer depth;
	private Integer parentIdx;
	private String type;
	
//	public Category toEntity() {
//		Category category = Category.builder()
//								.categoryIdx(categoryIdx) //Entity의 변수명 (내가 갖고있는 변수명)
//								.name(name)
//								.depth(depth)
//								.parentIdx(parentIdx)
//								.type(type)
//								.build();
////		if(parentIdx != null) {
////			category.setParentIdx(Category.builder().parentIdx(parentIdx).build());
////		}
//		return category;
//	}
}
