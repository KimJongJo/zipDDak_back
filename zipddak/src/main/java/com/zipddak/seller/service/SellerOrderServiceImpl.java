package com.zipddak.seller.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zipddak.dto.OrderDto;
import com.zipddak.dto.OrderItemDto;
import com.zipddak.entity.OrderItem;
import com.zipddak.entity.OrderItem.OrderStatus;
import com.zipddak.repository.OrderItemRepository;
import com.zipddak.repository.OrderRepository;
import com.zipddak.seller.dto.SaveResultDto;
import com.zipddak.seller.dto.SearchConditionDto;
import com.zipddak.seller.repository.SellerOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerOrderServiceImpl implements SellerOrderService {

	private final OrderRepository order_repo;
	private final OrderItemRepository orderItem_repo;
	private final SellerOrderRepository sellerOrder_repo;

	//주문 리스트 
	@Override
	public Map<String, Object> getMyOrderList(String sellerUsername, Integer page,  SearchConditionDto scDto) throws Exception {
		PageRequest pr = PageRequest.of(page - 1, 10);
		
		List<OrderDto> myOrderList = sellerOrder_repo.searchMyOrders(sellerUsername, pr, scDto);  //주문리스트
		Long myOrderCount = sellerOrder_repo.countMyOrders(sellerUsername, scDto);	//주문서 개수 

        int allPage = (int) Math.ceil(myOrderCount / 10.0);
        int startPage = (page - 1) / 10 * 10 + 1;
        int endPage = Math.min(startPage + 9, allPage);

        Map<String, Object> result = new HashMap<>();
        result.put("curPage", page);
        result.put("allPage", allPage);
        result.put("startPage", startPage);
        result.put("endPage", endPage);
        result.put("myOrderList", myOrderList);
        result.put("myOrderCount", myOrderCount);
        
		return result;
	}

	// 주문 내역 상세보기 
	@Override
	public Map<String, Object> getMyOrderDetail(String sellerUsername, Integer orderIdx) throws Exception {
		
		// 주문정보(orderIdx 하나니까 orders에서 order 정보 하나 가져오기)
//		Order order = order_repo.findById(orderIdx).orElseThrow(() -> new Exception("주문 없음"));
		OrderDto orderDto = sellerOrder_repo.findByOrderId(orderIdx);
		if (orderDto == null) {
	        throw new Exception("주문 없음");
	    }
				
				
		// 주문상품정보 (셀러 소유 OrderItem만 가져오기)
	    List<OrderItemDto> itemList = sellerOrder_repo.findMyOrderItems(sellerUsername, orderIdx);
	    if (itemList.isEmpty()) {
	        throw new Exception("해당 주문은 이 셀러의 상품이 아님");
	    }
	    
//	    System.out.println("order" + order.toDto());
//	    System.out.println("itemList" + itemList);

	    Map<String, Object> result = new HashMap<>();
	    result.put("orderData", orderDto);
	    result.put("myOrderItemList", itemList);

	    return result;
	}

	//운송장 등록
	@Override
	@Transactional
	public SaveResultDto registerTrackingNo(Integer orderIdx, List<Integer> itemIdxs, String postComp, String trackingNumber) throws Exception {
		// 해당 주문의 해당 itemIdx 목록만 조회
	    List<OrderItem> Orderitems = orderItem_repo.findOrderItemIdxByOrderIdxAndOrderItemIdxIn(orderIdx, itemIdxs);

		Integer successCnt = 0;
	    
        if (Orderitems.isEmpty()) {
            throw new IllegalArgumentException("운송장을 등록할 상품이 없습니다.");
//        	 return new SaveResultDto(false, null, "운송장을 등록할 상품이 없습니다.");
        }

        //각 orderItem에 운송장 등록 
        for (OrderItem item : Orderitems) {
        	System.out.println(item.getOrderStatus());
            // 상태 검사 
            if (!item.getOrderStatus().equals(OrderStatus.상품준비중)) {
                throw new IllegalStateException("상품준비중일때만 운송장 등록 가능: " + item.getOrderItemIdx());
//            	return new SaveResultDto(false,item.getOrderItemIdx(),"상품준비중일때만 운송장 등록 가능: " + item.getOrderItemIdx());
            }
            
            item.setPostComp(postComp);
            item.setTrackingNo(trackingNumber);
            item.setFirstShipDate(LocalDate.now());
            item.setOrderStatus(OrderStatus.배송중); // 상태 변경
            item = orderItem_repo.save(item); //db저장
            
            successCnt++;
        }
        
//        System.out.println("Orderitems.size() : " + Orderitems.size());
//        System.out.println("successCnt : " + successCnt);
        
        //요청한 상품의 개수와 db변경된 개수가 일치할경우(모두 성공)
        if(Orderitems.size() == successCnt) {
        	 return new SaveResultDto(true,null,"요청한 상품의 운송장 등록이 완료되었습니다");
        }else {
        	return new SaveResultDto(false,null,"운송장 등록 실패");
        }
	}


}
