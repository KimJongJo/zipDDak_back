package com.zipddak.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zipddak.admin.dto.OrderListDto;
import com.zipddak.admin.dto.OrderListToListDto;
import com.zipddak.admin.repository.ProductDslRepository;
import com.zipddak.entity.Cart;
import com.zipddak.entity.Product;
import com.zipddak.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	
	// 장바구니에 물건들 추가하기
	@Override
	public void addCart(OrderListToListDto orderListDto) throws Exception {

		List<OrderListDto> productList = orderListDto.getOrderListDto();
		String username = orderListDto.getUsername();
		
		for(OrderListDto product : productList) {
			
			Cart cart = Cart.builder()
							.optionIdx(product.getOptionId())
							.product(Product.builder()
									.productIdx(product.getProductId())
									.build())
							.quantity(product.getCount())
							.userUsername(username)
							.build();
			
			cartRepository.save(cart);
		}
		
	}

}
