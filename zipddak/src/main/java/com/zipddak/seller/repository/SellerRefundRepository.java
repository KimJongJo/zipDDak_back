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
import com.zipddak.entity.QClaimFile;
import com.zipddak.entity.QOrder;
import com.zipddak.entity.QOrderItem;
import com.zipddak.entity.QProduct;
import com.zipddak.entity.QProductFile;
import com.zipddak.entity.QProductOption;
import com.zipddak.entity.QRefund;
import com.zipddak.entity.QSeller;
import com.zipddak.entity.QUser;
import com.zipddak.entity.Refund;
import com.zipddak.seller.dto.SearchConditionDto;
import com.zipddak.seller.dto.SellerOrderAmountDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SellerRefundRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private List<OrderItem.OrderStatus> refundStatuses = Arrays.asList(OrderItem.OrderStatus.반품요청,
																		OrderItem.OrderStatus.반품회수, 
																		OrderItem.OrderStatus.반품완료, 
																		OrderItem.OrderStatus.반품거절);

	// ============================
	// 주문건 중 반품요청,진행 리스트 조회
	// ============================
	public List<RefundDto> searchMyRefunds(String sellerUsername, PageRequest pr, SearchConditionDto scDto) {
		QRefund refund = QRefund.refund;
		QOrder order = QOrder.order;
		QOrderItem item = QOrderItem.orderItem;
		QProduct product = QProduct.product;

		return jpaQueryFactory
				.select(Projections.fields(RefundDto.class, 
						refund.refundIdx, refund.orderIdx, 
						refund.createdAt,
						order.orderCode, 
						order.createdAt.as("orderDate"), 
						product.name.min().as("refundProductName"),
						order.user.username.as("username"), 
						refund.pickupTrackingNo.as("pickupTrackingNo"),
						refund.pickupPostComp.as("pickupPostComp"),
						item.orderStatus.stringValue().min().as("orderStatus"),
						Expressions.numberTemplate(Integer.class, "count({0})", item.orderItemIdx).as("refundItemCount")))
				.from(refund)
				.join(order).on(order.orderIdx.eq(refund.orderIdx))
				.join(item).on(order.orderIdx.eq(item.orderIdx))
				.join(product).on(product.productIdx.eq(item.product.productIdx))
				.where(item.orderStatus.in(refundStatuses), 
						product.sellerUsername.eq(sellerUsername),
						QPredicate.eq(order.user.username, scDto.getCustomerUsername()),
						QPredicate.anyContains(scDto.getKeyword(), order.orderCode, order.postRecipient, order.phone, product.name),
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
	//주문정보 
	public RefundDto findRefundOrderId(String sellerUsername, Integer refundIdx) {
		QOrder order = QOrder.order;
		QRefund refund = QRefund.refund;
		QClaimFile refundImage1  = new QClaimFile("refundImage1");
		QClaimFile refundImage2  = new QClaimFile("refundImage2");
		QClaimFile refundImage3  = new QClaimFile("refundImage3");
		
		return jpaQueryFactory.select(Projections.fields(RefundDto.class,
										refund.refundIdx,
										refund.orderIdx,
										refund.reasonType,
										refund.reasonDetail,
										refund.image1Idx,
										refund.image2Idx,
										refund.image3Idx,
										refund.shippingChargeType.stringValue().as("shippingChargeType"),
										refund.returnShippingFee,
										refund.refundAmount,
										refund.pickupPostComp,
										refund.pickupTrackingNo,
										refund.createdAt,
										order.user.username,
										order.orderCode,
										order.postZonecode,
										order.postAddr1,
										order.postAddr1,
										order.postRecipient.as("customerName"),
										order.phone.as("customerPhone"),
										refundImage1.fileRename.as("refundImage1"),
										refundImage2.fileRename.as("refundImage2"),
										refundImage3.fileRename.as("refundImage3")
								))
								.from(refund)
								.join(order).on(order.orderIdx.eq(refund.orderIdx))
								.leftJoin(refundImage1).on(refundImage1.claimFileIdx.eq(refund.image1Idx))
								.leftJoin(refundImage2).on(refundImage2.claimFileIdx.eq(refund.image1Idx))
								.leftJoin(refundImage3).on(refundImage3.claimFileIdx.eq(refund.image1Idx))
								.where(refund.refundIdx.eq(refundIdx))
								.fetchOne();
	}
	//반품 요청된 주문상품 정보 
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
	

	//구매자가 주문한 주문상품의 특정 셀러 총금액확인용 (무료배송 여부 확인용)
	public SellerOrderAmountDto findSellerOrderAmount(String sellerUsername, Integer orderIdx) {
		QOrder order = QOrder.order;
		QOrderItem item = QOrderItem.orderItem;
		QProduct product = QProduct.product;
		QSeller seller = QSeller.seller;
		
		return jpaQueryFactory.select(Projections.constructor(SellerOrderAmountDto.class,
															order.orderIdx,
															item.unitPrice.multiply(item.quantity).sum().as("sellerProductTotal"),
															seller.user.username,
													        seller.basicPostCharge,
													        seller.freeChargeAmount,
													        product.postCharge.as("singlePostCarge")
								))
								.from(order)
								.join(item).on(item.orderIdx.eq(order.orderIdx))
								.join(product).on(product.productIdx.eq(item.product.productIdx))
								.join(seller).on(seller.user.username.eq(product.sellerUsername))
								.where(order.orderIdx.eq(orderIdx), seller.user.username.eq(sellerUsername))
								.groupBy(order.orderIdx,seller.user.username,seller.basicPostCharge,seller.freeChargeAmount)
								.fetchOne();
		
	}
	
	
	//특정 반품건의 반품상품 금액만 계산
	public Long findRefundProductTotal(String sellerUsername, Integer refundIdx, Integer orderIdx) {
	    QOrderItem item = QOrderItem.orderItem;
	    QProduct product = QProduct.product;

	    Long result = jpaQueryFactory.select(item.unitPrice.multiply(item.quantity).sum())
						        .from(item)
						        .join(product).on(product.productIdx.eq(item.product.productIdx))
						        .where(item.orderIdx.eq(orderIdx),
						        		product.sellerUsername.eq(sellerUsername),
						        		item.refundIdx.eq(refundIdx)
						        )
						        .fetchOne();
	    
	    return result != null ? result : 0L;
	}
	
	
	// ============================
	// 문자열 → Enum 변환
	// ============================
	private List<Refund.RefundShippingChargeType> convertToEnum(List<String> strList) {
		if (strList == null || strList.isEmpty())
			return null;

		return strList.stream().filter(s -> s != null && !s.isBlank()).map(s -> {
			try {
				return Refund.RefundShippingChargeType.valueOf(s.trim());
			} catch (Exception e) {
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	

}
