package com.zipddak.seller.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingManageDto {

    private String orderCode;        // 주문번호
    private String productName;      // 상품명
    private String trackingNo;       // 최초 송장번호
    private String postComp;         // 택배사
    private String orderStatus;      // 주문상태
    private Date orderDate;          // 주문일자
}
