package com.zipddak.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionListDto {

	private Integer optionId; // 옵션Id
	private String name; // 옵션명
	private String value; // 선택값
	private long price; // 선택가격
	private Integer count; // 개수
	
}
