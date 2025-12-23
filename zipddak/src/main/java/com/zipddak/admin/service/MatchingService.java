package com.zipddak.admin.service;

import com.zipddak.admin.dto.EstimatePaymentStep1Dto;
import com.zipddak.entity.Matching;

public interface MatchingService {

	void createMatching(EstimatePaymentStep1Dto paymentDto, String orderId) throws Exception;

	Matching checkMatchingState(Integer estimateIdx) throws Exception;

}
