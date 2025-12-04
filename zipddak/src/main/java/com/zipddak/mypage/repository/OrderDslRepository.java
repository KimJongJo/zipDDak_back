package com.zipddak.mypage.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.entity.QOrder;
import com.zipddak.entity.QOrderItem;
import com.zipddak.entity.QProduct;
import com.zipddak.entity.QProductFile;
import com.zipddak.entity.QProductOption;
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
		QProductOption productOption = QProductOption.productOption;
		QProductFile productFile = QProductFile.productFile;

		return jpaQueryFactory
				.select(Projections.bean(OrderItemDto.class, product.productIdx, product.name.as("productName"),
						productOption.name.as("optionName"), orderItem.quantity, orderItem.unitPrice.as("price"),
						productFile.storagePath.as("thumbnail"), orderItem.orderStatus,
						productOption.name.as("exchangeOption")))
				.from(orderItem).leftJoin(order).on(order.orderIdx.eq(orderItem.orderIdx)).leftJoin(product)
				.on(product.productIdx.eq(orderItem.productIdx)).leftJoin(seller)
				.on(seller.username.eq(orderItem.sellerUsername)).leftJoin(productOption)
				.on(productOption.productOptionIdx.eq(orderItem.productOptionIdx)).leftJoin(productFile)
				.on(productFile.productFileIdx.eq(product.thumbnailFileIdx)).where(orderItem.orderIdx.eq(orderIdx))
				.fetchOne();
	}
}
