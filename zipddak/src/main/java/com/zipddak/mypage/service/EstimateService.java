package com.zipddak.mypage.service;

import com.zipddak.mypage.dto.EstimateWriteDto;

public interface EstimateService {
	void writeEstimate(EstimateWriteDto estimateWriteDto) throws Exception;
}
