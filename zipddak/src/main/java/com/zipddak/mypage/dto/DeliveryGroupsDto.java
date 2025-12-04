package com.zipddak.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryGroupsDto {
	private String brandName; // 브랜드 이름
	private String deliveryType; // “택배배송" | "직접픽업”
	private String deliveryFeeType; // “FREE" | "INDIVIDUAL" | "COMBINED”
	private int deliveryFeePrice; // 상품 기본 배송비 금액
	private int appliedDeliveryFee; // 실제 고객에게 부과된 배송비(계산된 값)
	private Object items[]; // 동일 브랜드 & 배송 타입인 주문 상품들
}
