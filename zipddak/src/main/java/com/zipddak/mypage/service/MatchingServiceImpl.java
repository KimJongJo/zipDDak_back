package com.zipddak.mypage.service;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.zipddak.entity.Expert;
import com.zipddak.entity.Matching.MatchingStatus;
import com.zipddak.mypage.dto.MatchingListDto;
import com.zipddak.mypage.dto.MatchingStatusSummaryDto;
import com.zipddak.repository.ExpertRepository;
import com.zipddak.repository.MatchingDslRepository;
import com.zipddak.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

	private final MatchingDslRepository matchingDslRepository;
	private final ExpertRepository expertRepository;

	// [전문가]매칭 목록 조회
	@Override
	public List<MatchingListDto> getExpertMatchingList(String username, MatchingStatus status, PageInfo pageInfo,
			Date startDate, Date endDate) throws Exception {
		Expert expert = expertRepository.findByUser_Username(username).get();

		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);

		List<MatchingListDto> matchingList = matchingDslRepository.selectExpertMatchingList(expert.getExpertIdx(),
				status, pageRequest, startDate, endDate);

		Long cnt = matchingDslRepository.selectExpertMatchingCount(expert.getExpertIdx(), status, startDate, endDate);

		Integer allPage = (int) (Math.ceil(cnt.doubleValue() / pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);

		return matchingList;
	}

	// [전문가]매칭현황 요약
	@Override
	public MatchingStatusSummaryDto getMatchingStatusSummary(String username) throws Exception {
		Expert expert = expertRepository.findByUser_Username(username).get();

		return matchingDslRepository.selectMatchingStatusSummary(expert.getExpertIdx());
	}

	// [일반사용자]매칭 목록 조회
	@Override
	public List<MatchingListDto> getUserMatchingList(String username, PageInfo pageInfo, Date startDate, Date endDate)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
