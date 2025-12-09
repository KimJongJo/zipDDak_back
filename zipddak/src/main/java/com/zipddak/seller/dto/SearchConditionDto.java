package com.zipddak.seller.dto;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchConditionDto {

	private String keyword;               // 공통 검색어
    private Date searchDate;              // 특정 날짜 검색
    private List<String> stateList;       // 공통 상태 (배송/주문/상품 상태 전부 포함)
    private List<Integer> categoryList;   // 카테고리 번호
    private String sellerUsername;        // 셀러
    private String customerUsername;      // 구매자
    private List<Integer> visibleList;    // 숫자 상태 (상품 visible)

}

