package com.zipddak.mypage.service;

import java.sql.Date;
import java.util.List;

import com.zipddak.entity.Matching.MatchingStatus;
import com.zipddak.mypage.dto.MatchingListDto;
import com.zipddak.mypage.dto.MatchingStatusSummaryDto;
import com.zipddak.util.PageInfo;

public interface MatchingService {
	public List<MatchingListDto> getExpertMatchingList(String username, MatchingStatus status, PageInfo pageInfo, Date startDate, Date endDate)
			throws Exception;
	
	public MatchingStatusSummaryDto getMatchingStatusSummary(String username) throws Exception;
	
	public List<MatchingListDto> getUserMatchingList(String username, PageInfo pageInfo, Date startDate, Date endDate)
			throws Exception;
}
