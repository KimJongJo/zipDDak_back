package com.zipddak.mypage.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.entity.OrderItem.OrderStatus;
import com.zipddak.entity.QOrder;
import com.zipddak.entity.QOrderItem;
import com.zipddak.entity.QProduct;
import com.zipddak.entity.QProductFile;
import com.zipddak.entity.QProductOption;
import com.zipddak.entity.QReviewProduct;
import com.zipddak.entity.QSeller;
import com.zipddak.mypage.dto.OrderItemDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderDslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public OrderItemDto selectOrderItem(Integer orderIdx) throws Exception {
		QOrder order = QOrder.order;
		QOrderItem orderItem = QOrderItem.orderItem;
		QProduct product = QProduct.product;
		QSeller seller = QSeller.seller;
		QProductOption productOption1 = new QProductOption("productOption1");
		QProductOption productOption2 = new QProductOption("productOption2");
		QProductFile productFile = QProductFile.productFile;
		QReviewProduct reviewProduct = QReviewProduct.reviewProduct;

		return jpaQueryFactory
				.select(Projections.bean(OrderItemDto.class, product.productIdx, product.name.as("productName"),
						productOption1.name.as("optionName"), orderItem.quantity, orderItem.unitPrice.as("price"),
						productFile.storagePath.as("thumbnail"), orderItem.orderStatus,
//						orderItem.orderStatus.eq(OrderStatus.배송완료)
//								.and(JPAExpressions.selectOne().from(reviewProduct)
//										.where(reviewProduct.orderIdx.eq(orderIdx)
//												.and(reviewProduct.productIdx.eq(product.productIdx)))
//										.notExists())
//								.as("reviewAvailable"),
						productOption2.name.as("exchangeOption")))
				.from(orderItem).leftJoin(order).on(order.orderIdx.eq(orderItem.orderIdx)).leftJoin(product)
//				.on(product.productIdx.eq(orderItem.productIdx)).leftJoin(seller)
				.on(seller.username.eq(orderItem.sellerUsername)).leftJoin(productOption1)
				.on(productOption1.productOptionIdx.eq(orderItem.productOptionIdx)).leftJoin(productOption2)
				.on(productOption2.productOptionIdx.eq(orderItem.exchangeNewOptIdx)).leftJoin(productFile)
				.on(productFile.productFileIdx.eq(product.thumbnailFileIdx)).where(orderItem.orderIdx.eq(orderIdx))
				.fetchOne();
	}
}
