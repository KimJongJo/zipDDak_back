package com.zipddak.dto;

import java.sql.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
    private Integer orderItemIdx;
    private Integer orderIdx;
    private String sellerUsername;
    private Integer productOptionIdx;
    private Long unitPrice;
    private Integer quantity;
    private String receiveWay;
    private String postComp;
    private String trackingNo;
    private String orderStatus;
    private Integer refundIdx;
    private Integer exchangeIdx;
    private Integer exchangeNewOptIdx;
    private Date createdAt;
    
    private Integer productIdx;
    private String name;
}
