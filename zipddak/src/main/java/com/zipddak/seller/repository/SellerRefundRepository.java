package com.zipddak.seller.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.dto.OrderDto;
import com.zipddak.dto.OrderItemDto;
import com.zipddak.dto.RefundDto;
import com.zipddak.entity.OrderItem;
import com.zipddak.entity.QOrder;
import com.zipddak.entity.QOrderItem;
import com.zipddak.entity.QProduct;
import com.zipddak.entity.QProductOption;
import com.zipddak.entity.QRefund;
import com.zipddak.entity.QUser;
import com.zipddak.seller.dto.SearchConditionDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SellerRefundRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private List<OrderItem.OrderStatus> refundStatuses = Arrays.asList(OrderItem.OrderStatus.반품요청,
			OrderItem.OrderStatus.반품회수, OrderItem.OrderStatus.반품완료, OrderItem.OrderStatus.반품거절);

	// ============================
	// 주문건 중 반품요청,진행 리스트 조회
	// ============================
	public List<RefundDto> searchMyRefunds(String sellerUsername, PageRequest pr, SearchConditionDto scDto) {
		QRefund refund = QRefund.refund;
		QOrder order = QOrder.order;
		QOrderItem item = QOrderItem.orderItem;
		QProduct product = QProduct.product;

		return jpaQueryFactory
				.select(Projections.fields(RefundDto.class, refund.refundIdx, refund.orderIdx, refund.createdAt,
						order.orderCode, order.createdAt.as("orderDate"), product.name.min().as("refundProductName"),
						order.user.username.as("username"), refund.pickupTrackingNo.as("pickupTrackingNo"),
						refund.pickupPostComp.as("pickupPostComp"),
						item.orderStatus.stringValue().min().as("orderStatus"),
						Expressions.numberTemplate(Integer.class, "count({0})", item.orderItemIdx)
								.as("refundItemCount")))
				.from(refund).join(order).on(order.orderIdx.eq(refund.orderIdx)).join(item)
				.on(order.orderIdx.eq(item.orderIdx)).join(product).on(product.productIdx.eq(item.product.productIdx))
				.where(item.orderStatus.in(refundStatuses), product.sellerUsername.eq(sellerUsername),
						QPredicate.eq(order.user.username, scDto.getCustomerUsername()),
						QPredicate.anyContains(scDto.getKeyword(), order.orderCode, order.postRecipient, order.phone,
								product.name),
						QPredicate.dateEq(order.createdAt, scDto.getSearchDate()))
				.orderBy(item.createdAt.desc()).offset(pr.getOffset()).limit(pr.getPageSize()).fetch();
	}

	public Long countMyRefunds(String sellerUsername, SearchConditionDto scDto) {
		QOrderItem item = QOrderItem.orderItem;

		return jpaQueryFactory.select(item.count()).from(item).where(item.orderStatus.in(refundStatuses)).fetchOne();
	}

	// ============================
	// 주문건 중 반품요청,진행 상세보기 조회 (주문정보 + 주문아이템 정보 ) 
	// ============================
	public RefundDto findRefundOrderId(String sellerUsername, Integer refundIdx) {
		QOrder order = QOrder.order;
		QUser user = QUser.user;
		QRefund refund = QRefund.refund;
		
		return jpaQueryFactory.select(Projections.fields(RefundDto.class,
										refund.refundIdx,
										refund.orderIdx,
										refund.reasonType,
										refund.reasonDetail,
										refund.image1Idx,
										refund.image2Idx,
										refund.image3Idx,
										refund.shippingChargeType,
										refund.returnShippingFee,
										refund.refundAmount,
										refund.pickupPostComp,
										refund.pickupTrackingNo,
										refund.createdAt,
										user.username,
										user.name.as("customerName"),
										user.phone.as("customerPhone")
								))
								.from(refund)
								.join(order).on(order.orderIdx.eq(refund.orderIdx))
								.join(user).on(order.user.username.eq(user.username))
								.where(refund.refundIdx.eq(refundIdx))
								.fetchOne();
	}

	public List<OrderItemDto> findRefundOrderItemList(String sellerUsername, Integer refundIdx) {
		QOrderItem item = QOrderItem.orderItem;
		QProduct product = QProduct.product;
		QProductOption pdOption =  QProductOption.productOption;
		QRefund refund = QRefund.refund;
		
		
		return jpaQueryFactory.select(Projections.fields(OrderItemDto.class,
		        item.orderItemIdx,
		        item.orderIdx,
		        item.productOptionIdx,
		        item.quantity,
		        item.unitPrice,
		        item.orderStatus.stringValue().as("orderStatus"),
		        item.postComp,
		        item.trackingNo,
		        product.name.as("productName"),
		        product.postCharge, 
		        product.postType.stringValue().as("postType"), // 배송방식 
		        pdOption.name.as("optionName"), // 옵션명 추가
		        pdOption.value.as("optionValue"), //옵션선택종류
		        pdOption.price.as("optionPrice") //옵션 추가가격 
		))
		.from(item)
		.join(product).on(item.product.productIdx.eq(product.productIdx))
		.join(pdOption).on(item.productOptionIdx.eq(pdOption.productOptionIdx)) 
		.where(product.sellerUsername.eq(sellerUsername)
		        .and(item.refundIdx.eq(refundIdx)))
		.fetch();
	}

}
