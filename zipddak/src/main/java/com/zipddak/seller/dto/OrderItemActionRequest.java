package com.zipddak.seller.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderItemActionRequest {
	
	private Integer orderIdx;
	private List<Integer> itemIdxs;
	private String postComp; // 운송장 등록에서만 사용(택배사)
    private String trackingNumber;  // 운송장 등록에서만 사용(송장번호)

}
