package com.zipddak.seller.repository;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.dto.RefundDto;
import com.zipddak.entity.OrderItem;
import com.zipddak.entity.QOrder;
import com.zipddak.entity.QOrderItem;
import com.zipddak.entity.QProduct;
import com.zipddak.entity.QRefund;
import com.zipddak.seller.dto.SearchConditionDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SellerRefundRepository {
	
	private final JPAQueryFactory jpaQueryFactory;
	
	
	private List<OrderItem.OrderStatus> refundStatuses 
											= Arrays.asList(OrderItem.OrderStatus.반품요청, 
															OrderItem.OrderStatus.반품회수, 
															OrderItem.OrderStatus.반품완료,
															OrderItem.OrderStatus.반품거절);


	// ============================
	// 주문건 중 배송진행 리스트 조회
	// ============================
	public List<RefundDto> searchMyRefunds(String sellerUsername, PageRequest pr, SearchConditionDto scDto) {
		QRefund refund = QRefund.refund;
		QOrder order = QOrder.order;
		QOrderItem item = QOrderItem.orderItem;
		QProduct product = QProduct.product;

		 return jpaQueryFactory.select(Projections.fields(RefundDto.class,
				 						refund.refundIdx.as("refundIdx"),
				 						refund.orderIdx.as("orderIdx"),
					                    order.orderCode.as("orderCode"),
					                    order.createdAt.as("orderDate"),
					                    product.name.min().as("refundProductName"), // 대표 상품명
					                    order.user.username.as("customerUsername"), //구매자
					                    refund.pickupTrackingNo.as("pickupTrackingNo"),
					                    refund.pickupPostComp.as("pickupPostComp"),
					                    item.orderStatus.min().as("orderStatus"),	// 대표 처리 상태
					                    item.countDistinct().as("refundItemCount")  // 반품 상품 개수
				  						))	
				 				.from(refund)
					            .join(order).on(order.orderIdx.eq(refund.orderIdx))
					            .join(item).on(order.orderIdx.eq(item.orderIdx))
					            .join(product).on(product.productIdx.eq(item.product.productIdx))
					            .where(item.orderStatus.in(refundStatuses),
					            		product.sellerUsername.eq(sellerUsername),
										QPredicate.eq(order.user.username, scDto.getCustomerUsername()),
										QPredicate.anyContains(scDto.getKeyword(), 
																order.orderCode, 
																order.postRecipient, 
																order.phone,
																product.name),
										QPredicate.dateEq(order.createdAt, scDto.getSearchDate()))
					            .orderBy(item.createdAt.desc())
					            .offset(pr.getOffset())
					            .limit(pr.getPageSize())
					            .fetch();
	}

	public Long countMyRefunds(String sellerUsername, SearchConditionDto scDto) {
		QOrderItem item = QOrderItem.orderItem;
		
		return jpaQueryFactory.select(item.count())
					            .from(item)
					            .where(item.orderStatus.in(refundStatuses))
					            .fetchOne();
	}
	

}
