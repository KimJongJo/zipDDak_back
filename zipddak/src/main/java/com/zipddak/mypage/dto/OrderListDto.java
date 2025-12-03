package com.zipddak.mypage.dto;

import java.sql.Date;

public class OrderListDto {
	private int orderId; // 주문 아이디
	private Date orderDate; // 주문날짜
	private Boolean canCancel; // 취소 가능여부
	private Boolean canReturn; // 교환환불 가능여부
	private Object deliveryGroups[]; // 주문상품들
}
