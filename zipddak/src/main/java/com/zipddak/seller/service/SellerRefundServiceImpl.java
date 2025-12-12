package com.zipddak.seller.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zipddak.dto.RefundDto;
import com.zipddak.entity.OrderItem;
import com.zipddak.entity.Product;
import com.zipddak.entity.OrderItem.OrderStatus;
import com.zipddak.repository.OrderItemRepository;
import com.zipddak.seller.dto.SaveResultDto;
import com.zipddak.seller.dto.SearchConditionDto;
import com.zipddak.seller.dto.ShippingManageDto;
import com.zipddak.seller.repository.SellerCommonRepository;
import com.zipddak.seller.repository.SellerOrderRepository;
import com.zipddak.seller.repository.SellerRefundRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerRefundServiceImpl implements SellerRefundService {

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

	// 환불처리
//	@Override
//	@Transactional
//	public SaveResultDto refundItems(List<Integer> itemIdxs) throws Exception {
//
//		// 해당 주문의 해당 itemIdx 목록만 조회
//	    List<OrderItem> Orderitems = orderItem_repo.findOrderItemIdxByOrderIdxAndOrderItemIdxIn(orderIdx, itemIdxs);
//		Boolean saveResult = false;
//		Integer idx = 0;
//		String msg = "";
//
//		if (Orderitems.isEmpty()) {
//			throw new IllegalArgumentException("환불할 상품이 없습니다.");
//		}
//
//		// 전체 리스트 검증 (DB 변경 X)
//	    for (OrderItem item : Orderitems) {
//
//	        // 이미 환불 또는 반품 완료 상태 체크
//	        if (item.getOrderStatus().equals(OrderStatus.반품완료)) {
//	        	saveResult = false;
//				idx = item.getOrderItemIdx();
//				msg = "이미 반품처리된 상품은 환불 불가. (상품번호: " + item.getOrderItemIdx() + ")";
//	        }
//
//	        // 상태 변경
//	        item.setOrderStatus(OrderStatus.반품완료);
//	        item = orderItem_repo.save(item);
//	        
//	        //요청한 상품의 개수와 db변경된 개수가 일치할경우(모두 성공)
//	        if(Orderitems.size() == item.) {
//	        	saveResult = true;
//				idx = item.getOrderItemIdx();
//				msg = "요청한 상품의 환불처리가 완료되었습니다";
//	        }else {
//	        	msg = "환불 처리 실패.";
//	        }
//	    }
//
//	    return new SaveResultDto(saveResult, idx, msg);
//	   
//	}

}
