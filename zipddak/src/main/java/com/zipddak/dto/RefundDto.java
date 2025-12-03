package com.zipddak.dto;

import java.sql.Date;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundDto {
    private Integer refundIdx;
    private Integer orderIdx;
    private String reasonType;
    private String reasonDetail;
    private Integer image1Idx;
    private Integer image2Idx;
    private Integer image3Idx;
    private String shippingChargeType;
    private Integer returnShippingFee;
    private Integer refundAmount;
    private String pickupPostComp;
    private String pickupTrackingNo;
    private Date createdAt;
}
