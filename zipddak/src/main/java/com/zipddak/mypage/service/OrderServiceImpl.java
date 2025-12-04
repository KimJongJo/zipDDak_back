package com.zipddak.mypage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zipddak.entity.Order;
import com.zipddak.entity.OrderItem;
import com.zipddak.entity.OrderItem.OrderStatus;
import com.zipddak.mypage.dto.DeliveryGroupsDto;
import com.zipddak.mypage.dto.OrderListDto;
import com.zipddak.repository.OrderItemRepository;
import com.zipddak.repository.OrderRepository;
import com.zipddak.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductRepository productRepository;

	@Override
	public List<OrderListDto> getOrderList(String username) throws Exception {
		// 가공된 내 주문목록
		List<OrderListDto> orderListDtoList = null;

		// 내 주문목록
		List<Order> orderList = orderRepository.findByUserUsername(username);

		for (Order order : orderList) {
			List<DeliveryGroupsDto> deliveryGroupsDtoList = null;

			// 하나의 주문을 OrderListDto 타입으로 변환
			OrderListDto orderListDto = order.toOrderListDto();
			orderListDto.setCanCancel(false);
			orderListDto.setCanReturn(false);

			// 하나의 주문에 해당하는 주문상품목록
			List<OrderItem> orderItemList = orderItemRepository.findByOrderIdx(order.getOrderIdx());

			for (OrderItem orderItem : orderItemList) {
				// 주문상품 중 "상품준비중"이 하나라도 있으면 취소 가능
				if (orderItem.getOrderStatus() == OrderStatus.상품준비중) {
					orderListDto.setCanCancel(true);
				}
				// 주문상품 중 "배송중", "배송완료"가 하나라도 있으면 교환/환불 가능
				if (orderItem.getOrderStatus() == OrderStatus.배송중 || orderItem.getOrderStatus() == OrderStatus.배송완료) {
					orderListDto.setCanReturn(true);
				}

				// 주문상품을 브랜드와 배송타입, 배송비부과타입 별로 분리
			}

		}

		return null;
	}

}