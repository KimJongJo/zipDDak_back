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
		// 媛�怨듬맂 �궡 二쇰Ц紐⑸줉
		List<OrderListDto> orderListDtoList = null;

		// �궡 二쇰Ц紐⑸줉
		List<Order> orderList = orderRepository.findByUserUsername(username);

		for (Order order : orderList) {
			List<DeliveryGroupsDto> deliveryGroupsDtoList = null;

			// �븯�굹�쓽 二쇰Ц�쓣 OrderListDto ���엯�쑝濡� 蹂��솚
			OrderListDto orderListDto = order.toOrderListDto();
			orderListDto.setCanCancel(false);
			orderListDto.setCanReturn(false);

			// �븯�굹�쓽 二쇰Ц�뿉 �빐�떦�븯�뒗 二쇰Ц�긽�뭹紐⑸줉
			List<OrderItem> orderItemList = orderItemRepository.findByOrderIdx(order.getOrderIdx());

			for (OrderItem orderItem : orderItemList) {
				// 二쇰Ц�긽�뭹 以� "�긽�뭹以�鍮꾩쨷"�씠 �븯�굹�씪�룄 �엳�쑝硫� 痍⑥냼 媛��뒫
//				if (orderItem.getOrderStatus() == OrderStatus.�긽�뭹以�鍮꾩쨷) {
//					orderListDto.setCanCancel(true);
//				}
//				// 二쇰Ц�긽�뭹 以� "諛곗넚以�", "諛곗넚�셿猷�"媛� �븯�굹�씪�룄 �엳�쑝硫� 援먰솚/�솚遺� 媛��뒫
//				if (orderItem.getOrderStatus() == OrderStatus.諛곗넚以� || orderItem.getOrderStatus() == OrderStatus.諛곗넚�셿猷�) {
//					orderListDto.setCanReturn(true);
//				}

				// 二쇰Ц�긽�뭹�쓣 釉뚮옖�뱶�� 諛곗넚���엯, 諛곗넚鍮꾨�怨쇳��엯 蹂꾨줈 遺꾨━
			}

		}

		return null;
	}

}
