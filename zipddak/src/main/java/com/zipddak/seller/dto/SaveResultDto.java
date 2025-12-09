package com.zipddak.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveResultDto {
	
	private boolean success; //성공여부
	private Integer productIdx;	//product 등록시 productIdx
	private String message;	//메세지 

}
