package com.zipddak.seller.service;

import java.util.List;
import java.util.Map;

import com.zipddak.seller.dto.OrderItemActionRequestDto;
import com.zipddak.seller.dto.SaveResultDto;
import com.zipddak.seller.dto.SearchConditionDto;

public interface SellerRefundService {

	//반품 진행 리스트 
	Map<String, Object> getMyRefundList(String sellerUsername, Integer page, SearchConditionDto scDto) throws Exception;
	//반품요청 상세보기  
	Map<String, Object> getRefundReqDetail(String sellerUsername, Integer refundIdx);
	//환불처리
	SaveResultDto refundItems(Integer orderIdx, List<Integer> itemIdxs) throws Exception;
	//반품 거절 처리 
	SaveResultDto refundRejectItems(OrderItemActionRequestDto reqItems);


}
