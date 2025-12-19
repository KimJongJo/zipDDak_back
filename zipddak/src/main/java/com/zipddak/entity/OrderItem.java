package com.zipddak.entity;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import com.zipddak.dto.OrderItemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Entity
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderItemIdx;

	@Column(nullable = false)
	private Integer orderIdx;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "productIdx")
	private Product product;

	@Column
	private Integer productOptionIdx;

	@Column(nullable = false)
	private Long unitPrice;

	@Column(nullable = false)
	private Integer quantity;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReceiveWay receiveWay; // POST, PICKUP

	@Column
	private String postComp;

	@Column
	private String trackingNo;
	
	@Column
	private LocalDate firstShipDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus orderStatus;

	@Column
	private Integer refundIdx;

	@Column
	private Integer exchangeIdx;

	@Column
	private Integer exchangeNewOptIdx;
	
	@Column
	private Integer cancelIdx;
	
	@CreationTimestamp
	private Date createdAt;
	
	@Column
	private LocalDate exchangeRejectedAt;  //교환 거절일자 (판매자)
	
	@Column
	private LocalDate exchangeAcceptedAt;	//교환 접수수락일자 (판매자) = 수거요청일자
	
	@Column
	private LocalDate exchangePickupComplatedAt; //교환 수거완료일자 (판매자)
	
	@Column
	private LocalDate resendAt; //교환 재배송 일자 (판매자)
	
	@Column
	private LocalDate exchangeComplatedAt;  //교환 처리완료 일자 
	
	@Column
	private LocalDate refundRejectedAt;  //반품 거절일자 (판매자)
	
	@Column
	private LocalDate refundAcceptedAt;	//반품 접수수락일자 (판매자) = 수거요청일자
	
	@Column
	private LocalDate refundPickupComplatedAt; //반품 수거완료일자 (판매자)
	
	@Column
	private LocalDate refundComplatedAt;  //반품 처리완료 일자 
	

	public enum ReceiveWay {
		post, pickup
	}

	public enum OrderStatus {
		상품준비중, 배송중, 배송완료, 취소완료, 교환요청, 교환회수, 교환발송, 교환완료, 교환거절, 반품요청, 반품회수, 반품완료, 반품거절, 결제취소
	}

	public OrderItemDto toDto() {
		return OrderItemDto.builder().orderItemIdx(orderItemIdx).orderIdx(orderIdx).productOptionIdx(productOptionIdx)
				.unitPrice(unitPrice).quantity(quantity).receiveWay(receiveWay.toString()).postComp(postComp)
				.trackingNo(trackingNo).orderStatus(orderStatus.toString()).refundIdx(refundIdx)
				.exchangeIdx(exchangeIdx).exchangeNewOptIdx(exchangeNewOptIdx).createdAt(createdAt)
				.productIdx(product.getProductIdx()).name(product.getName()).sellerUsername(product.getSellerUsername())
				.build();
	}
}
