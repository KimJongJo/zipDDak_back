package com.zipddak.seller.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.zipddak.dto.RefundDto;
import com.zipddak.seller.dto.SearchConditionDto;
import com.zipddak.seller.dto.ShippingManageDto;
import com.zipddak.seller.repository.SellerOrderRepository;
import com.zipddak.seller.repository.SellerRefundRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerRefundServiceImpl implements SellerRefundService {
	
	private final SellerRefundRepository sellerRefund_repo;

	@Override
	public Map<String, Object> getMyRefundList(String sellerUsername, Integer page, SearchConditionDto scDto) throws Exception {
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

}
