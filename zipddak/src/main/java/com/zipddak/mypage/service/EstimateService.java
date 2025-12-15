package com.zipddak.mypage.service;

import java.util.List;

import com.zipddak.mypage.dto.EstimateWriteDto;
import com.zipddak.mypage.dto.SentEstimateListDto;
import com.zipddak.util.PageInfo;

public interface EstimateService {
	void writeEstimate(EstimateWriteDto estimateWriteDto) throws Exception;
	
	List<SentEstimateListDto> getExpertSentEstimateList(String username, PageInfo pageInfo, String status) throws Exception;
}
