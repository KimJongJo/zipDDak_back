package com.zipddak.seller.dto;

import java.time.LocalDateTime;

import com.zipddak.entity.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderSummaryDto {

    private Long orderIdx;
    private String buyerUsername;
    private OrderItem.OrderStatus orderState;
    private LocalDateTime orderDate;

    private Long itemCount;    // 주문별 상품 개수
}
