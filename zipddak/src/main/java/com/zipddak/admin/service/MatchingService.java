package com.zipddak.admin.service;

import com.zipddak.admin.dto.EstimatePaymentStep1Dto;

public interface MatchingService {

	void createMatching(EstimatePaymentStep1Dto paymentDto, String orderId) throws Exception;

}
