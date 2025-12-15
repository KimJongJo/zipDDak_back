package com.zipddak.seller.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.dto.OrderDto;
import com.zipddak.dto.OrderItemDto;
import com.zipddak.entity.Order;
import com.zipddak.entity.OrderItem;
import com.zipddak.entity.QOrder;
import com.zipddak.entity.QOrderItem;
import com.zipddak.entity.QProduct;
import com.zipddak.entity.QProductOption;
import com.zipddak.entity.QUser;
import com.zipddak.seller.dto.SearchConditionDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SellerOrderRepository {

	private final JPAQueryFactory jpaQueryFactory;
	



	// ============================
	// 주문 리스트 조회
	// ============================
	public List<OrderDto> searchMyOrders(String sellerUsername, PageRequest pageRequest, SearchConditionDto scDto) {

		QOrder order = QOrder.order;
		QOrderItem item = QOrderItem.orderItem;
		QProduct product = QProduct.product;

		List<OrderItem.OrderStatus> enumList = convertToEnum(scDto.getOrderStateList());

		return jpaQueryFactory.select(Projections.fields(OrderDto.class, 
											order.orderIdx, 
											order.orderCode, 
											order.totalAmount,
											order.createdAt, 
											order.user.username.as("customerUsername"),
											item.orderStatus.min().as("orderStatus"),	// 대표 주문 상태
											product.name.min().as("productName"),	// 대표 상품명
											item.countDistinct().as("itemCount")))  // 주문 상품 개수 추가
								.from(order)
								.join(item).on(item.orderIdx.eq(order.orderIdx))
								.join(product).on(product.productIdx.eq(item.product.productIdx))
								.where(product.sellerUsername.eq(sellerUsername),
										item.orderStatus.ne(OrderItem.OrderStatus.결제대기),
										QPredicate.eq(order.user.username, scDto.getCustomerUsername()),
										QPredicate.inEnum(item.orderStatus, enumList),
										QPredicate.anyContains(scDto.getKeyword(), 
																order.orderCode, 
																order.postRecipient, 
																order.phone,
																product.name),
										QPredicate.dateEq(order.createdAt, scDto.getSearchDate()))
								.groupBy(order.orderIdx, order.orderCode, order.totalAmount, order.createdAt, order.user.username)
								.orderBy(order.orderIdx.desc()).offset(pageRequest.getOffset()).limit(pageRequest.getPageSize())
								.fetch();
	}

	// ============================
	// 주문 개수
	// ============================
	public Long countMyOrders(String sellerUsername, SearchConditionDto scDto) {

		QOrder order = QOrder.order;
		QOrderItem item = QOrderItem.orderItem;
		QProduct product = QProduct.product;

		List<OrderItem.OrderStatus> enumList = convertToEnum(scDto.getOrderStateList());

		return jpaQueryFactory.select(order.countDistinct()).
								from(order)
								.join(item).on(item.orderIdx.eq(order.orderIdx))
								.join(product).on(product.productIdx.eq(item.product.productIdx))
								.where(product.sellerUsername.eq(sellerUsername),
										QPredicate.eq(order.user.username, scDto.getCustomerUsername()),
										QPredicate.inEnum(item.orderStatus, enumList), 
										QPredicate.anyContains(scDto.getKeyword(),
																order.orderCode, 
																order.postRecipient, 
																order.phone, 
																product.name),
										QPredicate.dateEq(order.createdAt, scDto.getSearchDate()))
								.fetchOne();
	}
	
	

	// ============================
	// 주문 상세보기 (주문정보 + 주문아이템 정보 ) 
	// ============================
	//주문 정보 
	public OrderDto findByOrderId(Integer orderIdx) {
		QOrder order = QOrder.order;
		QOrderItem item = QOrderItem.orderItem;
		QUser user = QUser.user;
		
		return jpaQueryFactory.select(Projections.fields(OrderDto.class,
										order.orderIdx,
										order.orderCode,
										order.subtotalAmount,
										order.shippingAmount,
										order.totalAmount,
										order.postZonecode,
										order.postAddr1,
										order.postAddr2,
										order.phone,
										order.postRecipient,
										order.postNote,
										order.createdAt,
//										item.orderStatus.stringValue().min().as("orderStatus"),
										user.username.as("customerUsername"),
										user.name.as("customerName"),
										user.phone.as("customerPhone")
								))
								.from(order)
//								.join(item).on(item.orderIdx.eq(order.orderIdx))
								.join(user).on(order.user.username.eq(user.username))
								.where(order.orderIdx.eq(orderIdx))
								.fetchOne();
	}
	
	//주문아이템 정보 
	public List<OrderItemDto> findMyOrderItems(String sellerUsername, Integer orderIdx) {
		QOrderItem item = QOrderItem.orderItem;
		QProduct product = QProduct.product;
		QProductOption pdOption =  QProductOption.productOption;
		
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
								        .and(item.orderIdx.eq(orderIdx)))
								.fetch();
	}
	
	

	// ============================
	// 문자열 → Enum 변환
	// ============================
	private List<OrderItem.OrderStatus> convertToEnum(List<String> strList) {
		if (strList == null || strList.isEmpty())
			return null;

		return strList.stream().filter(s -> s != null && !s.isBlank()).map(s -> {
			try {
				return OrderItem.OrderStatus.valueOf(s.trim());
			} catch (Exception e) {
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	

}