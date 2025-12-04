package com.zipddak.mypage.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
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
import com.zipddak.mypage.dto.OrderItemFlatDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderDslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public List<OrderItemFlatDto> selectOrderItemFlatList(String username, PageRequest pageRequest, Date startDate,
			Date endDate) throws Exception {
		QOrder order = QOrder.order;
		QOrderItem orderItem = QOrderItem.orderItem;
		QProduct product = QProduct.product;
		QSeller seller = QSeller.seller;
		QProductOption productOption1 = new QProductOption("productOption1");
		QProductOption productOption2 = new QProductOption("productOption2");
		QProductFile productFile = QProductFile.productFile;
		QReviewProduct reviewProduct = QReviewProduct.reviewProduct;

		BooleanExpression isReviewable = orderItem.orderStatus.eq(OrderStatus.배송완료).and(JPAExpressions.selectOne()
				.from(reviewProduct).where(reviewProduct.orderItemIdx.eq(orderItem.orderItemIdx)).notExists());

		if (startDate != null) {
			return jpaQueryFactory
					.select(Projections.constructor(OrderItemFlatDto.class, order.orderIdx, order.createdAt,
							seller.brandName, orderItem.receiveWay, product.postType, product.postCharge,
							seller.freeChargeAmount, product.productIdx, product.name, productOption1.name,
							orderItem.quantity, orderItem.unitPrice, productFile.storagePath, orderItem.orderStatus,
							new CaseBuilder().when(isReviewable).then(true).otherwise(false).as("reviewAvailable"),
							productOption2.name))
					.from(orderItem).leftJoin(order).on(order.orderIdx.eq(orderItem.orderIdx)).leftJoin(product)
					.on(product.productIdx.eq(orderItem.product.productIdx)).leftJoin(seller)
					.on(seller.username.eq(product.sellerUsername)).leftJoin(productOption1)
					.on(productOption1.productOptionIdx.eq(orderItem.productOptionIdx)).leftJoin(productOption2)
					.on(productOption2.productOptionIdx.eq(orderItem.exchangeNewOptIdx)).leftJoin(productFile)
					.on(productFile.productFileIdx.eq(product.thumbnailFileIdx)).where(order.user.username.eq(username))
					.offset(pageRequest.getOffset()).limit(pageRequest.getPageSize()).fetch();
		} else if (endDate != null) {
			return jpaQueryFactory
					.select(Projections.constructor(OrderItemFlatDto.class, order.orderIdx, order.createdAt,
							seller.brandName, orderItem.receiveWay, product.postType, product.postCharge,
							seller.freeChargeAmount, product.productIdx, product.name, productOption1.name,
							orderItem.quantity, orderItem.unitPrice, productFile.storagePath, orderItem.orderStatus,
							new CaseBuilder().when(isReviewable).then(true).otherwise(false).as("reviewAvailable"),
							productOption2.name))
					.from(orderItem).leftJoin(order).on(order.orderIdx.eq(orderItem.orderIdx)).leftJoin(product)
					.on(product.productIdx.eq(orderItem.product.productIdx)).leftJoin(seller)
					.on(seller.username.eq(product.sellerUsername)).leftJoin(productOption1)
					.on(productOption1.productOptionIdx.eq(orderItem.productOptionIdx)).leftJoin(productOption2)
					.on(productOption2.productOptionIdx.eq(orderItem.exchangeNewOptIdx)).leftJoin(productFile)
					.on(productFile.productFileIdx.eq(product.thumbnailFileIdx)).where(order.user.username.eq(username))
					.offset(pageRequest.getOffset()).limit(pageRequest.getPageSize()).fetch();
		} else if (startDate != null & endDate != null) {
			return jpaQueryFactory
					.select(Projections.constructor(OrderItemFlatDto.class, order.orderIdx, order.createdAt,
							seller.brandName, orderItem.receiveWay, product.postType, product.postCharge,
							seller.freeChargeAmount, product.productIdx, product.name, productOption1.name,
							orderItem.quantity, orderItem.unitPrice, productFile.storagePath, orderItem.orderStatus,
							new CaseBuilder().when(isReviewable).then(true).otherwise(false).as("reviewAvailable"),
							productOption2.name))
					.from(orderItem).leftJoin(order).on(order.orderIdx.eq(orderItem.orderIdx)).leftJoin(product)
					.on(product.productIdx.eq(orderItem.product.productIdx)).leftJoin(seller)
					.on(seller.username.eq(product.sellerUsername)).leftJoin(productOption1)
					.on(productOption1.productOptionIdx.eq(orderItem.productOptionIdx)).leftJoin(productOption2)
					.on(productOption2.productOptionIdx.eq(orderItem.exchangeNewOptIdx)).leftJoin(productFile)
					.on(productFile.productFileIdx.eq(product.thumbnailFileIdx)).where(order.user.username.eq(username))
					.offset(pageRequest.getOffset()).limit(pageRequest.getPageSize()).fetch();
		} else {
			return jpaQueryFactory
					.select(Projections.constructor(OrderItemFlatDto.class, order.orderIdx, order.createdAt,
							seller.brandName, orderItem.receiveWay, product.postType, product.postCharge,
							seller.freeChargeAmount, product.productIdx, product.name, productOption1.name,
							orderItem.quantity, orderItem.unitPrice, productFile.storagePath, orderItem.orderStatus,
							new CaseBuilder().when(isReviewable).then(true).otherwise(false).as("reviewAvailable"),
							productOption2.name))
					.from(orderItem).leftJoin(order).on(order.orderIdx.eq(orderItem.orderIdx)).leftJoin(product)
					.on(product.productIdx.eq(orderItem.product.productIdx)).leftJoin(seller)
					.on(seller.username.eq(product.sellerUsername)).leftJoin(productOption1)
					.on(productOption1.productOptionIdx.eq(orderItem.productOptionIdx)).leftJoin(productOption2)
					.on(productOption2.productOptionIdx.eq(orderItem.exchangeNewOptIdx)).leftJoin(productFile)
					.on(productFile.productFileIdx.eq(product.thumbnailFileIdx)).where(order.user.username.eq(username))
					.offset(pageRequest.getOffset()).limit(pageRequest.getPageSize()).fetch();
		}

	}

	public Long selectOrderItemFlatCount(String username) throws Exception {
		QOrder order = QOrder.order;
		QOrderItem orderItem = QOrderItem.orderItem;

		return jpaQueryFactory.select(orderItem.count()).from(orderItem).leftJoin(order)
				.on(order.orderIdx.eq(orderItem.orderIdx)).where(order.user.username.eq(username)).fetchOne();
	}
}
