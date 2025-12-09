package com.zipddak.admin.service;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zipddak.admin.dto.OrderListDto;
import com.zipddak.admin.dto.PaymentComplateDto;
import com.zipddak.admin.repository.ProductDslRepository;
import com.zipddak.entity.Order;
import com.zipddak.entity.Payment;
import com.zipddak.entity.Order.PaymentStatus;
import com.zipddak.entity.OrderItem.OrderStatus;
import com.zipddak.entity.OrderItem;
import com.zipddak.repository.OrderItemRepository;
import com.zipddak.repository.OrderRepository;
import com.zipddak.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final ProductDslRepository productDslRepository;
	
	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	
	@Value("${toss-payment-secret-key}")
	private String tossSecretKey;

	// 결제 금액 계산
	@Override
	public Integer getTotalPrice(Integer productId, List<OrderListDto> orderList, Integer postCharge) {
		
		Integer productPrice = (int)productDslRepository.getProductPrice(productId);
		
		Integer amount = 0;
		
		for(OrderListDto orderListDto : orderList) {
			amount += (orderListDto.getPrice() + productPrice) * orderListDto.getCount();
		}
		
		amount += postCharge;
		
		return amount;
	}

	
	// orderName 생성
	@Override
	public String getOrderName(Integer productId, List<OrderListDto> orderList) {
		
		String productName = productDslRepository.getProductName(productId);
		
		int sum = 0;
		for(OrderListDto orderDto : orderList) {
			sum += orderDto.getCount();
		}
		
		// 상품 수량이 1개 이상이면
		if(sum > 1){
			return productName + "(" + orderList.size() + ")개";
		}else {
			return productName;
		}
		
	}


	// 결제 최종 승인
	@Override
	public void approvePayment(PaymentComplateDto paymentComplateDto) throws Exception {
		
		// Toss 결제 승인 api url
		String url = "https://api.tosspayments.com/v1/payments/" + paymentComplateDto.getPaymentKey();
		
		// 헤더 설정 -> 시크릿 키
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(tossSecretKey, "");
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		// body
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("orderId", paymentComplateDto.getOrderId());
		body.put("amount", paymentComplateDto.getAmount());
		
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
		
		// 결제 승인 요청
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters()
	    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		
		System.out.println(response);
		
		// response에 가져온 데이터를 꺼내야함
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(response.getBody());
		
		// 만약에 결제가 성공 된다면 추가할 데이터
		
		if(response.getStatusCode() == HttpStatus.OK) {
			
			OffsetDateTime requestedAtOdt = OffsetDateTime.parse(jsonNode.path("requestedAt").asText());
			OffsetDateTime approvedAtOdt = OffsetDateTime.parse(jsonNode.path("approvedAt").asText());

			// Timestamp로 변환
			Timestamp requestedAt = Timestamp.from(requestedAtOdt.toInstant());
			Timestamp approvedAt = Timestamp.from(approvedAtOdt.toInstant());
			
			// 결제 데이터 (결제 테이블)
			Payment payment = Payment.builder()
									.paymentKey(jsonNode.path("paymentKey").asText())
									.type(jsonNode.path("type").asText())
									.orderId(jsonNode.path("orderId").asText())
									.orderName(jsonNode.path("orderName").asText())
									.mId(jsonNode.path("mId").asText())
									.method(jsonNode.path("method").asText())
									.totalAmount(jsonNode.path("totalAmount").asInt())
									.balanceAmount(jsonNode.path("balanceAmount").asInt())
									.status(jsonNode.path("status").asText())
									.requestedAt(requestedAt)
									.approvedAt(approvedAt)
									.lastTransactionKey(jsonNode.path("lastTransactionKey").asText())
									.isPartialCancelable(jsonNode.path("isPartialCancelable").asBoolean())
									.receiptUrl(jsonNode.path("receipt").path("url").asText())
									.cardAmount(jsonNode.path("card").path("amount").asInt())
									.cardIssuerCode(jsonNode.path("card").path("issuerCode").asText())
									.cardAcquirerCode(jsonNode.path("card").path("acquirerCode").asText())
									.cardNumber(jsonNode.path("card").path("number").asText())
									.cardInstallmentPlanMonths(jsonNode.path("card").path("installmentPlanMonths").asInt())
									.easypayProvider(jsonNode.path("easyPay").path("provider").asText())
									.easypayAmount(jsonNode.path("easyPay").path("amount").asInt())
									.build();
			
			Payment savedPayment = paymentRepository.save(payment);
			
			// 주문 데이터에서 결제 완료로 업데이트 그리고 payment_idx도 넣어야함
			
			Order order = orderRepository.findByOrderCode(jsonNode.path("orderId").asText());
			order.setPaymentStatus(PaymentStatus.결제완료);
			order.setPaymentIdx(savedPayment.getPaymentIdx());
			
			orderRepository.save(order);
			
			// 각 상품에 대한 주문 상품 정보에서 주문 상태를 상품 준비중으로 업데이트
			List<OrderItem> orderItems = orderItemRepository.findByOrderIdx(order.getOrderIdx());
			
			for(OrderItem orderItem : orderItems) {
				orderItem.setOrderStatus(OrderStatus.상품준비중);
				orderItemRepository.save(orderItem);
				
			}
			
			
		}
		
	}

}
