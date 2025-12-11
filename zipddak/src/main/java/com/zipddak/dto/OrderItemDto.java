package com.zipddak.dto;

import java.sql.Date;

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
	private Date firstShipDate; // 최초 출고일자
	private String orderStatus;
	private Integer refundIdx;
	private Integer exchangeIdx;
	private Integer exchangeNewOptIdx;
	private Date createdAt;

	private Integer productIdx;
	private String name;
	private String sellerUsername;
	private String customerUsername;
}
