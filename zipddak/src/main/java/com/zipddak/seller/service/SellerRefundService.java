package com.zipddak.seller.service;

import java.util.List;
import java.util.Map;

import com.zipddak.seller.dto.SaveResultDto;
import com.zipddak.seller.dto.SearchConditionDto;

public interface SellerRefundService {

	//반품 진행 리스트 
	Map<String, Object> getMyRefundList(String sellerUsername, Integer page, SearchConditionDto scDto) throws Exception;

	//환불처리
//	SaveResultDto refundItems(List<Integer> itemIdxs) throws Exception;

}
