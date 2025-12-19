package com.zipddak.seller.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zipddak.dto.OrderDto;
import com.zipddak.dto.OrderItemDto;
import com.zipddak.dto.RefundDto;
import com.zipddak.entity.OrderItem;
import com.zipddak.entity.OrderItem.OrderStatus;
import com.zipddak.entity.Refund.RefundShippingChargeType;
import com.zipddak.repository.OrderItemRepository;
import com.zipddak.repository.RefundRepository;
import com.zipddak.seller.dto.OrderItemActionRequestDto;
import com.zipddak.seller.dto.SaveResultDto;
import com.zipddak.seller.dto.SearchConditionDto;
import com.zipddak.seller.dto.SellerOrderAmountDto;
import com.zipddak.seller.repository.SellerRefundRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerRefundServiceImpl implements SellerRefundService {

	private final RefundRepository refund_repo;
	private final OrderItemRepository orderItem_repo;
	private final SellerRefundRepository sellerRefund_repo;

	@Override
	public Map<String, Object> getMyRefundList(String sellerUsername, Integer page, SearchConditionDto scDto)
			throws Exception {
		PageRequest pr = PageRequest.of(page - 1, 10);

		List<RefundDto> myRefundList = sellerRefund_repo.searchMyRefunds(sellerUsername, pr, scDto); // 반품 진행 리스트
		Long myRefundCount = sellerRefund_repo.countMyRefunds(sellerUsername, scDto); // 반품 진행 개수

		int allPage = (int) Math.ceil(myRefundCount / 10.0);
		int startPage = (page - 1) / 10 * 10 + 1;
		int endPage = Math.min(startPage + 9, allPage);

		Map<String, Object> result = new HashMap<>();
		result.put("curPage", page);
		result.put("allPage", allPage);
		result.put("startPage", startPage);
		result.put("endPage", endPage);
		result.put("myRefundList", myRefundList);
		result.put("myRefundCount", myRefundCount);

		return result;
	}
	
	//반품요청 상세보기
	@Override
	public Map<String, Object> getRefundReqDetail(String sellerUsername, Integer refundIdx) {
		// 반품 요청된 주문정보
		RefundDto refundOrderData = sellerRefund_repo.findRefundOrderId(sellerUsername, refundIdx);
		if (refundOrderData == null) {
	        throw new IllegalStateException("반품 요청 없음");
	    }
		
		// 반품요청된 주문상품정보 
		List<OrderItemDto> refundOrderItemList = sellerRefund_repo.findRefundOrderItemList(sellerUsername, refundIdx);
		    if (refundOrderItemList.isEmpty()) {
		        throw new IllegalStateException("해당 주문상품은 이 셀러의 상품이 아님");
		}
		    
		//구매자가 주문한 주문상품의 특정 셀러 총금액확인용 (무료배송 여부 확인용)
		SellerOrderAmountDto orderTotalAmountBySeller = sellerRefund_repo.findSellerOrderAmount(sellerUsername, refundOrderData.getOrderIdx());
		//이 반품건의 반품상품 금액만 계산
		Long refundProductTotalThisReq = sellerRefund_repo.findRefundProductTotal(sellerUsername, refundIdx, refundOrderData.getOrderIdx() );

		long reqRefundAmount;
		RefundShippingChargeType shippingChargeType = RefundShippingChargeType.valueOf(refundOrderData.getShippingChargeType());

		if (shippingChargeType == RefundShippingChargeType.BUYER) {
			reqRefundAmount = refundProductTotalThisReq - (orderTotalAmountBySeller.getBasicPostCharge() * 2);
		    //구매자 귀책 -> 환불금액 = 왕복배송비 제외 후 환불 
		} else {
			reqRefundAmount = refundProductTotalThisReq;
		    //판매자 귀책 -> 환불금액 = 상품 금액 
		}
		    
	    Map<String, Object> result = new HashMap<>();
	    result.put("refundOrderData", refundOrderData);
	    result.put("refundOrderItemList", refundOrderItemList);
	    result.put("refundProductTotal", refundProductTotalThisReq);
	    result.put("reqRefundAmount", reqRefundAmount);
		
		return result;
	}
	
	
	//반품 거절 처리 
	@Override
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
//	        item.setSellerMessageType(reqItems.getRejectReason());
//	        item.setSellerMessage(reqItems.getRejectDetailReason());
	        item = orderItem_repo.save(item);  //db저장
	        
	        successCnt++;
	    }

	  //요청한 상품의 개수와 db변경된 개수가 일치할경우(모두 성공)
        if(Orderitems.size() == successCnt) {
        	 return new SaveResultDto(true,null,"요청한 상품의 반품 거절 처리가 완료되었습니다");
        	 
        }else {
        	return new SaveResultDto(false,null,"요청 처리 실패");
        }
	}
	
	// 환불처리
	@Override
	@Transactional
	public SaveResultDto refundItems(Integer orderIdx, List<Integer> itemIdxs) throws Exception {

		// 해당 주문의 해당 itemIdx 목록만 조회
	    List<OrderItem> Orderitems = orderItem_repo.findOrderItemIdxByOrderIdxAndOrderItemIdxIn(orderIdx, itemIdxs);

	    Integer successCnt = 0;
	    
		if (Orderitems.isEmpty()) {
			throw new IllegalArgumentException("환불할 상품이 없습니다.");
		}

		// 전체 리스트 검증 (DB 변경 X)
	    for (OrderItem item : Orderitems) {
	    	System.out.println(item.getOrderStatus());

	        // 이미 환불 또는 반품 완료 상태 체크
	        if (item.getOrderStatus().equals(OrderStatus.반품완료)) {
	        	throw new IllegalStateException("이미 반품처리된 상품은 환불 불가. (상품번호: " + item.getOrderItemIdx() + ")");
	        }

	        // 상태 변경
	        item.setOrderStatus(OrderStatus.반품완료);
	        item = orderItem_repo.save(item);  //db저장
	        
	        successCnt++;
	    }

	  //요청한 상품의 개수와 db변경된 개수가 일치할경우(모두 성공)
        if(Orderitems.size() == successCnt) {
        	 return new SaveResultDto(true,null,"요청한 상품의 환불처리가 완료되었습니다");
        	 
        }else {
        	return new SaveResultDto(false,null,"환불 처리 실패");
        }
	   
	}
	



	

}
