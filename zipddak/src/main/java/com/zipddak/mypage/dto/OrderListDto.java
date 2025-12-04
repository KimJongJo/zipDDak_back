package com.zipddak.mypage.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderListDto {
	private int orderIdx; // 주문 아이디
	private Date orderDate; // 주문날짜
	private Boolean canCancel; // 취소 가능여부
	private Boolean canReturn; // 교환환불 가능여부
	private Object deliveryGroups[]; // 주문상품들
}
