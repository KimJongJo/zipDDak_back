package com.zipddak.dto;

import java.sql.Date;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
	private Integer orderItemIdx;
	private Integer orderIdx;
	private Integer productOptionIdx;
	private Long unitPrice;
	private Integer quantity;
	private String receiveWay;
	private String postComp;
	private String trackingNo; 
	private LocalDate firstShipDate; // 최초 출고일자
	private String orderStatus;
	private Integer refundIdx;
	private Integer exchangeIdx;
	private Integer exchangeNewOptIdx;
	private Date createdAt;
	

	//jon용 컬럼 
	private Integer productIdx;
	private String name;
	private String sellerUsername;
	private String customerUsername;
	private String postType;	//배송타입 (bundle / single)
	private String productName;	//상품명
	private Long postCharge;	//배송비
	private String optionName;	//옵션명
	private String optionValue;	//옵션선택값
	private Long optionPrice;	//옵션추가금액 
}
