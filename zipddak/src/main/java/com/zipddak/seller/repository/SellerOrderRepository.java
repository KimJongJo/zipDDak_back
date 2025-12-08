package com.zipddak.seller.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.dto.OrderDto;
import com.zipddak.dto.ProductDto;
import com.zipddak.entity.Order;
import com.zipddak.entity.QOrder;
import com.zipddak.entity.QOrderItem;
import com.zipddak.entity.QProduct;
import com.zipddak.entity.QUser;
import com.zipddak.seller.dto.SearchConditionDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SellerOrderRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public List<OrderDto> searchMyOrders(PageRequest pr, SearchConditionDto scDto) {

		QOrder order = QOrder.order;
		QOrderItem item = QOrderItem.orderItem;
		QProduct product = QProduct.product;

		return jpaQueryFactory.select(Projections.fields(OrderDto.class, 
													order.orderIdx, 
													order.orderCode, 
													order.totalAmount,
													order.createdAt, 
													order.user.username.as("customerUsername"), 
													item.orderStatus,
													product.name.as("productName")))
								.from(order).join(item).on(item.orderIdx.eq(order.orderIdx)).join(product)
								.on(product.productIdx.eq(item.product.productIdx))
								.where(QPredicate.eq(order.user.username, scDto.getCustomerUsername()),
										QPredicate.inEnum(item.orderStatus, scDto.getStateList()),
										QPredicate.anyContains(scDto.getKeyword(), order.orderCode, order.postRecipient, order.phone,
												product.name),
										QPredicate.dateEq(order.createdAt, scDto.getSearchDate()))
								.orderBy(order.orderIdx.desc()).offset(pr.getOffset()).limit(pr.getPageSize()).fetch();
	}

	public Long countMyOrders(SearchConditionDto scDto) {

		QOrder order = QOrder.order;
		QOrderItem item = QOrderItem.orderItem;
		QProduct product = QProduct.product;

		return jpaQueryFactory.select(order.countDistinct())
								.from(order).join(item).on(item.orderIdx.eq(order.orderIdx))
								.join(product).on(product.productIdx.eq(item.product.productIdx))
								.where(QPredicate.eq(order.user.username, scDto.getCustomerUsername()),
										QPredicate.inEnum(item.orderStatus, scDto.getStateList()),
										QPredicate.anyContains(scDto.getKeyword(), order.orderCode, order.postRecipient, order.phone,
												product.name),
										QPredicate.dateEq(order.createdAt, scDto.getSearchDate()))
								.fetchOne();
	}
}
