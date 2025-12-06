package com.zipddak.mypage.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.entity.OrderItem.OrderStatus;
import com.zipddak.entity.QExchange;
import com.zipddak.entity.QOrder;
import com.zipddak.entity.QOrderItem;
import com.zipddak.entity.QProduct;
import com.zipddak.entity.QProductFile;
import com.zipddak.entity.QProductOption;
import com.zipddak.entity.QRefund;
import com.zipddak.entity.QReviewProduct;
import com.zipddak.entity.QSeller;
import com.zipddak.mypage.dto.OrderItemFlatDto;
import com.zipddak.mypage.dto.OrderStatusSummaryDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderDslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	// 주문배송목록 조회
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
		QExchange exchange = QExchange.exchange;
		QRefund refund = QRefund.refund;

		// 택배사
		Expression<String> postCompExpr = new CaseBuilder().when(orderItem.orderStatus.eq(OrderStatus.교환회수))
				.then(exchange.pickupPostComp).when(orderItem.orderStatus.in(OrderStatus.교환발송, OrderStatus.교환완료))
				.then(exchange.reshipPostComp).when(orderItem.orderStatus.in(OrderStatus.반품회수, OrderStatus.반품완료))
				.then(refund.pickupPostComp).otherwise(orderItem.postComp);

		// 송장번호
		Expression<String> trackingNoExpr = new CaseBuilder().when(orderItem.orderStatus.eq(OrderStatus.교환회수))
				.then(exchange.pickupTrackingNo).when(orderItem.orderStatus.in(OrderStatus.교환발송, OrderStatus.교환완료))
				.then(exchange.reshipTrackingNo).when(orderItem.orderStatus.in(OrderStatus.반품회수, OrderStatus.반품완료))
				.then(refund.pickupTrackingNo).otherwise(orderItem.trackingNo);

		// 리뷰 가능 여부 조건
		BooleanExpression isReviewable = orderItem.orderStatus.eq(OrderStatus.배송완료).and(JPAExpressions.selectOne()
				.from(reviewProduct).where(reviewProduct.orderItemIdx.eq(orderItem.orderItemIdx)).notExists());

		BooleanBuilder builder = new BooleanBuilder();

		// 사용자 이름 조건
		builder.and(order.user.username.eq(username));

		// 날짜 조건
		if (startDate != null) {
			builder.and(orderItem.createdAt.goe(startDate));
		}
		if (endDate != null) {
			builder.and(orderItem.createdAt.loe(endDate));
		}

		return jpaQueryFactory
				.select(Projections.constructor(OrderItemFlatDto.class, order.orderIdx, order.orderCode,
						order.createdAt, seller.brandName, orderItem.receiveWay, product.postType, product.postCharge,
						seller.freeChargeAmount, orderItem.orderItemIdx, product.productIdx, product.name,
						productOption1.name, orderItem.quantity, orderItem.unitPrice, productFile.storagePath,
						trackingNoExpr, postCompExpr, orderItem.orderStatus,
						new CaseBuilder().when(isReviewable).then(true).otherwise(false).as("reviewAvailable"),
						productOption2.name))
				.from(orderItem).leftJoin(order).on(order.orderIdx.eq(orderItem.orderIdx)).leftJoin(product)
				.on(product.productIdx.eq(orderItem.product.productIdx)).leftJoin(seller)
				.on(seller.username.eq(product.sellerUsername)).leftJoin(productOption1)
				.on(productOption1.productOptionIdx.eq(orderItem.productOptionIdx)).leftJoin(productOption2)
				.on(productOption2.productOptionIdx.eq(orderItem.exchangeNewOptIdx)).leftJoin(productFile)
				.on(productFile.productFileIdx.eq(product.thumbnailFileIdx)).leftJoin(exchange)
				.on(exchange.orderIdx.eq(orderItem.orderIdx)).leftJoin(refund)
				.on(refund.orderIdx.eq(orderItem.orderIdx))

				.where(builder).offset(pageRequest.getOffset()).limit(pageRequest.getPageSize()).fetch();

	}

	// 주문배송목록 개수
	public Long selectOrderItemFlatCount(String username, Date startDate, Date endDate) throws Exception {
		QOrder order = QOrder.order;
		QOrderItem orderItem = QOrderItem.orderItem;

		BooleanBuilder builder = new BooleanBuilder();

		// 사용자 이름 조건
		builder.and(order.user.username.eq(username));

		// 날짜 조건
		if (startDate != null) {
			builder.and(orderItem.createdAt.goe(startDate));
		}
		if (endDate != null) {
			builder.and(orderItem.createdAt.loe(endDate));
		}

		return jpaQueryFactory.select(orderItem.count()).from(orderItem).leftJoin(order)
				.on(order.orderIdx.eq(orderItem.orderIdx)).where(builder).fetchOne();
	}

	// 취소교환반품목록 조회
	public List<OrderItemFlatDto> selectReturnOrderItemFlatList(String username, PageRequest pageRequest,
			Date startDate, Date endDate) throws Exception {

		QOrder order = QOrder.order;
		QOrderItem orderItem = QOrderItem.orderItem;
		QProduct product = QProduct.product;
		QSeller seller = QSeller.seller;
		QProductOption productOption1 = new QProductOption("productOption1");
		QProductOption productOption2 = new QProductOption("productOption2");
		QProductFile productFile = QProductFile.productFile;
		QExchange exchange = QExchange.exchange;
		QRefund refund = QRefund.refund;

		// 택배사
		Expression<String> postCompExpr = new CaseBuilder().when(orderItem.orderStatus.eq(OrderStatus.교환회수))
				.then(exchange.pickupPostComp).when(orderItem.orderStatus.in(OrderStatus.교환발송, OrderStatus.교환완료))
				.then(exchange.reshipPostComp).when(orderItem.orderStatus.in(OrderStatus.반품회수, OrderStatus.반품완료))
				.then(refund.pickupPostComp).otherwise(orderItem.postComp);

		// 송장번호
		Expression<String> trackingNoExpr = new CaseBuilder().when(orderItem.orderStatus.eq(OrderStatus.교환회수))
				.then(exchange.pickupTrackingNo).when(orderItem.orderStatus.in(OrderStatus.교환발송, OrderStatus.교환완료))
				.then(exchange.reshipTrackingNo).when(orderItem.orderStatus.in(OrderStatus.반품회수, OrderStatus.반품완료))
				.then(refund.pickupTrackingNo).otherwise(orderItem.trackingNo);

		BooleanBuilder builder = new BooleanBuilder();

		// 사용자 이름 조건
		builder.and(order.user.username.eq(username));

		// 주문 상태 조건
		builder.and(orderItem.orderStatus.ne(OrderStatus.상품준비중));
		builder.and(orderItem.orderStatus.ne(OrderStatus.배송중));
		builder.and(orderItem.orderStatus.ne(OrderStatus.배송완료));

		// 날짜 조건
		if (startDate != null) {
			builder.and(orderItem.createdAt.goe(startDate));
		}
		if (endDate != null) {
			builder.and(orderItem.createdAt.loe(endDate));
		}

		return jpaQueryFactory
				.select(Projections.constructor(OrderItemFlatDto.class, order.orderIdx, order.orderCode,
						order.createdAt, seller.brandName, orderItem.receiveWay, product.postType, product.postCharge,
						seller.freeChargeAmount, orderItem.orderItemIdx, product.productIdx, product.name,
						productOption1.name, orderItem.quantity, orderItem.unitPrice, productFile.storagePath,
						trackingNoExpr, postCompExpr, orderItem.orderStatus, Expressions.asBoolean(false),
						productOption2.name))
				.from(orderItem).leftJoin(order).on(order.orderIdx.eq(orderItem.orderIdx)).leftJoin(product)
				.on(product.productIdx.eq(orderItem.product.productIdx)).leftJoin(seller)
				.on(seller.username.eq(product.sellerUsername)).leftJoin(productOption1)
				.on(productOption1.productOptionIdx.eq(orderItem.productOptionIdx)).leftJoin(productOption2)
				.on(productOption2.productOptionIdx.eq(orderItem.exchangeNewOptIdx)).leftJoin(productFile)
				.on(productFile.productFileIdx.eq(product.thumbnailFileIdx)).leftJoin(exchange)
				.on(exchange.orderIdx.eq(orderItem.orderIdx)).leftJoin(refund)
				.on(refund.orderIdx.eq(orderItem.orderIdx)).where(builder).offset(pageRequest.getOffset())
				.limit(pageRequest.getPageSize()).fetch();

	}

	// 취소교환반품목록 개수
	public Long selectReturnOrderItemFlatCount(String username, Date startDate, Date endDate) throws Exception {
		QOrder order = QOrder.order;
		QOrderItem orderItem = QOrderItem.orderItem;

		BooleanBuilder builder = new BooleanBuilder();

		// 사용자 이름 조건
		builder.and(order.user.username.eq(username));

		// 주문 상태 조건
		builder.and(orderItem.orderStatus.ne(OrderStatus.상품준비중));
		builder.and(orderItem.orderStatus.ne(OrderStatus.배송중));
		builder.and(orderItem.orderStatus.ne(OrderStatus.배송완료));

		// 날짜 조건
		if (startDate != null) {
			builder.and(orderItem.createdAt.goe(startDate));
		}
		if (endDate != null) {
			builder.and(orderItem.createdAt.loe(endDate));
		}

		return jpaQueryFactory.select(orderItem.count()).from(orderItem).leftJoin(order)
				.on(order.orderIdx.eq(orderItem.orderIdx)).where(builder).fetchOne();
	}

	// 주문배송현황 요약
	public OrderStatusSummaryDto selectOrderStatusSummary(String username, Date todayDate, Date sixMonthsAgoDate)
			throws Exception {
		QOrder order = QOrder.order;
		QOrderItem orderItem = QOrderItem.orderItem;

		NumberExpression<Integer> returnsStatus = new CaseBuilder()
				.when(orderItem.orderStatus.notIn(OrderStatus.상품준비중, OrderStatus.배송중, OrderStatus.배송완료)).then(1)
				.otherwise(0);

		BooleanBuilder builder = new BooleanBuilder();

		// 사용자 이름 조건
		builder.and(order.user.username.eq(username));

		// 날짜 조건
		builder.and(orderItem.createdAt.between(sixMonthsAgoDate, todayDate));

		return jpaQueryFactory
				.select(Projections.constructor(OrderStatusSummaryDto.class,
						orderItem.orderStatus.when(OrderStatus.상품준비중).then(1).otherwise(0).sum(),
						orderItem.orderStatus.when(OrderStatus.배송중).then(1).otherwise(0).sum(),
						orderItem.orderStatus.when(OrderStatus.배송완료).then(1).otherwise(0).sum(), returnsStatus.sum()))
				.from(orderItem).leftJoin(order).on(order.orderIdx.eq(orderItem.orderIdx)).where(builder).fetchOne();
	}
}
