package com.zipddak.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCardDto {
	private Integer productIdx;
	private String name;
	private Integer discount;
	private Long salePrice;
	private String sellerUsername;
	private String fileRename;
	private String storagePath;
	private Double avgRating;
	private Long reviewCount;
	private String brandName;
}
