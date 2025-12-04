package com.zipddak.entity;

import java.sql.Date;

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
@DynamicInsert
@Entity
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderItemIdx;

	@Column(nullable = false)
	private Integer orderIdx;

	@Column(nullable = false)
	private String sellerUsername;

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

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus orderStatus;

	@Column
	private Integer refundIdx;

	@Column
	private Integer exchangeIdx;

	@Column
	private Integer exchangeNewOptIdx;

	@CreationTimestamp
	private Date createdAt;

	public enum ReceiveWay {
		post, pickup
	}

	public enum OrderStatus {
		상품준비중, 배송중, 배송완료, 교환요청, 교환회수, 교환발송, 교환완료, 교환거절, 반품요청, 반품회수, 반품완료, 반품거절
	}
}
