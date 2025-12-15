package com.zipddak.user.dto;

import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToolCardDto {
	
	 private Integer toolIdx;
	 private String name;
	 private Long rentalPrice;
	 private String addr1;
	 private String satus;
	 
	 private String fileRename; // 썸네일 파일 이름
	 private String storagePath; // 사진 저장 경로
	 
	 private Integer chatCount; //문의 수
	 private Boolean favorite; // 관심 표시
	 private Integer favoriteCount; //관심 수
	 
}
