package com.zipddak.seller.repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.dto.OrderDto;
import com.zipddak.entity.OrderItem;
import com.zipddak.entity.QOrder;
import com.zipddak.entity.QOrderItem;
import com.zipddak.entity.QProduct;
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