package com.zipddak.dto;

import java.sql.Date;

import com.zipddak.entity.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
	private Integer orderIdx;
	private String orderCode;
	private Long subtotalAmount;
	private Long shippingAmount;
	private Long totalAmount;
	private Integer paymentIdx;
	private String postZonecode;
	private String postAddr1;
	private String postAddr2;
	private String phone;
	private String postRecipient;
	private String postNote;
	private Date createdAt;
    
    private String username;
    private String name;
    
    //join용 컬럼
    
    private String customerUsername;	//order_item
    private OrderItem.OrderStatus orderStatus;  //order_item
    private String productIdx;	//product
    private String productName;	//product
    private Long itemCount;
    
    
}
