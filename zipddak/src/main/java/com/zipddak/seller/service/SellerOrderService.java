package com.zipddak.seller.service;

import java.util.List;
import java.util.Map;

import com.zipddak.dto.OrderDto;
import com.zipddak.seller.dto.SaveResultDto;
import com.zipddak.seller.dto.SearchConditionDto;

public interface SellerOrderService {

	//주문 리스트 보기
	Map<String, Object> getMyOrderList(String sellerUsername, Integer page, SearchConditionDto scDto) throws Exception;

	//주문내역 상세보기
	Map<String, Object> getMyOrderDetail(String sellerUsername, Integer orderIdx) throws Exception;

	//운송장 등록
	SaveResultDto registerTrackingNo(Integer orderIdx, List<Integer> itemIdxs, String postComp, String trackingNumber) throws Exception;

}
