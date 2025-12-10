package com.zipddak.admin.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.admin.dto.BrandDto;
import com.zipddak.admin.dto.OptionListDto;
import com.zipddak.admin.dto.OrderListDto;
import com.zipddak.admin.dto.PaymentComplateDto;
import com.zipddak.admin.dto.PaymentInfoDto;
import com.zipddak.admin.dto.RecvUserDto;
import com.zipddak.admin.dto.productPaymentStep1Dto;
import com.zipddak.admin.service.OrderService;
import com.zipddak.admin.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
	
	private final PaymentService paymentService;
	private final OrderService paymentOrderService;

	
	@Value("${react-server.uri}")
	private String reactServer;
	// 처음 결제를 요청할때 orderId를 생성하고
	// DB에서 금액을 다시 계산해서 amoun를 반환한다. -> front에서 가격을 조작할 수 있음
	// orderName은 알아서 작성
	
	@PostMapping("/product")
	public ResponseEntity<PaymentInfoDto> productPayment(@RequestBody productPaymentStep1Dto paymentDto){
		
		try {
			
			// orderId 생성
			String orderId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
	                + "-" + (int)(Math.random() * 9000 + 1000);
			
			List<BrandDto> brandList = paymentDto.getBrandList();
			
			// 총 결제 가격 계산
			Map<String, Long> amount = paymentService.getTotalPrice(brandList);
			
			// 상품 이름 + 옵션 개수 string 생성
			String orderName = paymentService.getOrderName(brandList);
			
			// 주문 테이블 + 주문 상품 테이블에 저장
			RecvUserDto recvUser = paymentDto.getRecvUser();
			String username = paymentDto.getUsername();
			paymentOrderService.addOrder(username, orderId, amount, recvUser, brandList);
			
			
			PaymentInfoDto paymentInfo = PaymentInfoDto.builder()
				.orderId(orderId)
				.amount(((Long) amount.get("totalPrice")).intValue())
				.orderName(orderName)
				.build();
			
			return ResponseEntity.ok(paymentInfo);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	// 서버에서 토스 최종 결제를 승인해야함
	@GetMapping("/complate")
	public ResponseEntity<?> paymentComplate(PaymentComplateDto paymentComplateDto){
		
		try {
			
			paymentService.approvePayment(paymentComplateDto);
			
			 // 클라이언트로 리다이렉트할 때 주문 ID 포함
		    String redirectUrl = reactServer + "zipddak/productOrderComplate?orderCode=" + paymentComplateDto.getOrderId();
			
			return ResponseEntity.status(HttpStatus.FOUND)
					.location(URI.create(redirectUrl))
					.build();
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
}
