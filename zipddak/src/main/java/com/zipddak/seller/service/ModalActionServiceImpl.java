package com.zipddak.seller.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zipddak.dto.RefundDto;
import com.zipddak.entity.Exchange;
import com.zipddak.entity.OrderItem;
import com.zipddak.entity.OrderItem.OrderStatus;
import com.zipddak.entity.Refund;
import com.zipddak.enums.TrackingRegistType;
import com.zipddak.repository.ExchangeRepository;
import com.zipddak.repository.OrderItemRepository;
import com.zipddak.repository.OrderRepository;
import com.zipddak.repository.RefundRepository;
import com.zipddak.seller.dto.CompletedResponseDto;
import com.zipddak.seller.dto.OrderItemActionRequestDto;
import com.zipddak.seller.dto.SaveResultDto;
import com.zipddak.seller.repository.SellerRefundRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModalActionServiceImpl implements ModalActionService {
	
	private final RefundRepository refund_repo;
	private final ExchangeRepository exchange_repo;
	private final OrderRepository order_repo;
	private final OrderItemRepository orderItem_repo;
	private final SellerRefundRepository sellerRefund_repo;
	
	//운송장 등록
	@Override
	@Transactional
	public SaveResultDto registerTrackingNo(OrderItemActionRequestDto reqItems) {
		// 해당 주문의 해당 itemIdx 목록만 조회
	    List<OrderItem> orderitems = orderItem_repo.findOrderItemIdxByOrderIdxAndOrderItemIdxIn(reqItems.getOrderIdx(), reqItems.getItemIdxs());

        if (orderitems.isEmpty()) {
            throw new IllegalArgumentException("운송장을 등록할 상품이 없습니다.");
        }
        
        TrackingRegistType type = reqItems.getRegistType();
        
        //각 orderItem에 운송장 등록 
        switch (type) {
	        case FIRST_SEND:
	            registFirstSend(orderitems, reqItems);
	            break;
	
	        case REFUND_PICKUP:
	            registReturnPickup(orderitems, reqItems);
	            break;
	
	        case EXCHANGE_PICKUP:
	            registExchangePickup(orderitems, reqItems);
	            break;
	
	        case EXCHANGE_SEND:
	            registExchangeSend(orderitems, reqItems);
	            break;
	
	        default:
	            throw new IllegalArgumentException("알 수 없는 운송장 등록 타입");
	    }
        
        return new SaveResultDto(true, null, "운송장 등록이 완료되었습니다.");
	}
	
	//최초발송 운송장등록 
	private void registFirstSend(List<OrderItem> items, OrderItemActionRequestDto req) {

	    for (OrderItem item : items) {

	        if (item.getOrderStatus() != OrderStatus.상품준비중) {
	            throw new IllegalStateException("상품준비중 상태에서만 최초 발송 가능: " + item.getOrderItemIdx());
	        }

	        item.setPostComp(req.getPostComp());
	        item.setTrackingNo(req.getTrackingNumber());
	        item.setFirstShipDate(LocalDate.now());
	        item.setOrderStatus(OrderStatus.배송중);

	        orderItem_repo.save(item);
	    }
	}
	//반품수거 운송장등록 
	private void registReturnPickup(List<OrderItem> items, OrderItemActionRequestDto req) {

	    Refund refund = refund_repo.findById(items.get(0).getRefundIdx()).orElseThrow(() -> new IllegalStateException("반품 정보 없음"));

	    if (refund.getPickupTrackingNo() != null) {
	        throw new IllegalStateException("이미 반품 수거 운송장이 등록됨");
	    }

	    refund.setPickupPostComp(req.getPostComp());
	    refund.setPickupTrackingNo(req.getTrackingNumber());

	    refund_repo.save(refund);
	}
	//교환 수거 운송장 등록 
	private void registExchangePickup(List<OrderItem> items, OrderItemActionRequestDto req) {

	    Exchange exchange = exchange_repo.findById(items.get(0).getExchangeIdx()).orElseThrow(() -> new IllegalStateException("교환 정보 없음"));

	    if (exchange.getPickupTrackingNo() != null) {
	        throw new IllegalStateException("이미 교환 수거 운송장 등록됨");
	    }

	    exchange.setPickupPostComp(req.getPostComp());
	    exchange.setPickupTrackingNo(req.getTrackingNumber());

	    exchange_repo.save(exchange);
	}
	//교환재발송 
	private void registExchangeSend(List<OrderItem> items, OrderItemActionRequestDto req) {

	    Exchange exchange = exchange_repo.findById(items.get(0).getExchangeIdx()).orElseThrow(() -> new IllegalStateException("교환 정보 없음"));

	    if (exchange.getReshipTrackingNo() != null) {
	        throw new IllegalStateException("이미 교환 재발송 운송장이 등록됨");
	    }

	    // 1. Exchange에 운송장 등록
	    exchange.setReshipPostComp(req.getPostComp());
	    exchange.setReshipTrackingNo(req.getTrackingNumber());
	    exchange_repo.save(exchange);

	    // 2. 관련 OrderItem에 재발송 일자 기록
	    for (OrderItem item : items) {
	        if (item.getOrderStatus() != OrderStatus.교환요청) {
	            throw new IllegalStateException("교환진행중 상태에서만 교환 재발송 가능: " + item.getOrderItemIdx());
	        }

	        item.setResendAt(LocalDate.now());
	        item.setOrderStatus(OrderStatus.배송중); // 재발송 후 상태
	        orderItem_repo.save(item);
	    }
	}
	
	//수거완료처리
	@Override
	@Transactional
	public CompletedResponseDto pickupComplated(OrderItemActionRequestDto req) {
		Integer refundIdx = req.getNum(); 
			if (refundIdx == null) {
				throw new IllegalArgumentException("refundIdx가 없습니다.");
			}
		// 1. refund 조회
	    Refund refund = refund_repo.findById(refundIdx).orElseThrow(() -> new IllegalArgumentException("반품 정보가 존재하지 않습니다."));

	    // 2. 해당 반품에 묶인 orderItem 조회
	    List<OrderItem> orderItems = orderItem_repo.findByRefundIdx(refundIdx);
	    if (orderItems.isEmpty()) {
	        throw new IllegalStateException("반품에 연결된 주문상품이 없습니다.");
	    }

	    // 3. orderItem 일괄 수거완료 처리
	    LocalDate today = LocalDate.now();

	    for (OrderItem item : orderItems) {
	        // 반품 수거완료일자
	        item.setRefundPickupComplatedAt(today);
	        // 주문 상태 변경
	        item.setOrderStatus(OrderStatus.반품회수);
	    }
		
		return new CompletedResponseDto(true, today);
	}
	
	//반품 거절 처리 
	@Override
	@Transactional
	public SaveResultDto refundRejectItems(OrderItemActionRequestDto reqItems) {
		// 해당 주문의 해당 itemIdx 목록만 조회
	    List<OrderItem> Orderitems = orderItem_repo.findOrderItemIdxByOrderIdxAndOrderItemIdxIn(reqItems.getOrderIdx(), reqItems.getItemIdxs());
	    Integer successCnt = 0;
	    
	    if (Orderitems.isEmpty()) {
			throw new IllegalArgumentException("처리할 상품이 없습니다.");
		}

		// 전체 리스트 검증 (DB 변경 X)
	    for (OrderItem item : Orderitems) {
	    	System.out.println(item.getOrderStatus());

	        // 이미 환불 또는 반품 완료 상태 체크
	        if (item.getOrderStatus().equals(OrderStatus.반품완료)) {
	        	throw new IllegalStateException("이미 반품처리된 상품은 환불 불가.");
	        }

	        // 상태 변경
	        item.setOrderStatus(OrderStatus.반품거절);
	        item.setRefundRejectedAt(LocalDate.now());
	        item = orderItem_repo.save(item);  //db저장
	        
	        System.out.println("itemsss : " + item);
	        
	        successCnt++;
	    }

	  //요청한 상품의 개수와 db변경된 개수가 일치할경우(모두 성공)
        if(Orderitems.size() == successCnt) {
        	 return new SaveResultDto(true,null,"요청한 상품의 반품 거절 처리가 완료되었습니다");
        	 
        }else {
        	return new SaveResultDto(false,null,"요청 처리 실패");
        }
	}
	
	
	//반품 접수 수락 처리 
	@Override
	@Transactional
	public SaveResultDto refundAcceptItems(OrderItemActionRequestDto reqItems) {
		// 해당 주문의 해당 itemIdx 목록만 조회
	    List<OrderItem> Orderitems = orderItem_repo.findOrderItemIdxByOrderIdxAndOrderItemIdxIn(reqItems.getOrderIdx(), reqItems.getItemIdxs());
	    Integer successCnt = 0;
	    
	    if (Orderitems.isEmpty()) {
			throw new IllegalArgumentException("처리할 상품이 없습니다.");
		}

		// 전체 리스트 검증 (DB 변경 X)
	    for (OrderItem item : Orderitems) {
	        // 이미 환불 또는 반품 완료 상태 체크
	        if (item.getOrderStatus().equals(OrderStatus.반품완료)) {
	        	throw new IllegalStateException("이미 반품처리된 상품은 접수 불가.");
	        }

	        // 상태 변경
	        item.setOrderStatus(OrderStatus.반품회수);
	        item.setRefundAcceptedAt(LocalDate.now()); //승인날짜 (=수거요청날짜) 
	        item = orderItem_repo.save(item);  //db저장
	        
	        System.out.println("item : " + item);
	        successCnt++;
	    }

	  //요청한 상품의 개수와 db변경된 개수가 일치할경우(모두 성공)
        if(Orderitems.size() == successCnt) {
        	 return new SaveResultDto(true,null,"요청한 상품의 반품 수거 요청 처리가 완료되었습니다");
        	 
        }else {
        	return new SaveResultDto(false,null,"요청 처리 실패");
        }
	}
	
	// 환불처리
	@Override
	@Transactional
	public SaveResultDto refundItems(Integer orderIdx, List<Integer> itemIdxs) {

		// 해당 주문의 해당 itemIdx 목록만 조회
	    List<OrderItem> orderItems = orderItem_repo.findOrderItemIdxByOrderIdxAndOrderItemIdxIn(orderIdx, itemIdxs);
		if (orderItems.isEmpty()) {
			throw new IllegalArgumentException("환불할 상품이 없습니다.");
		}

		// 전체 리스트 검증 (DB 변경 X)
	    for (OrderItem item : orderItems) {

	        // 이미 환불 또는 반품 완료 상태 체크
	        if (item.getOrderStatus() == OrderStatus.반품완료) {
	        	throw new IllegalStateException("이미 반품처리된 상품은 환불 불가. (상품번호: " + item.getOrderItemIdx() + ")");
	        }


	    }
	    
	    LocalDate today = LocalDate.now();
	    //상태 변경
	    for (OrderItem item : orderItems) {
	        item.setOrderStatus(OrderStatus.반품완료);
	        item.setRefundComplatedAt(today);
	    }
	    
	    return new SaveResultDto(true, null, "요청한 상품의 환불처리가 완료되었습니다.");
	}
}
