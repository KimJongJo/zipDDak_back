package com.zipddak.mypage.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.zipddak.entity.Expert;
import com.zipddak.mypage.dto.PublicRequestDetailDto;
import com.zipddak.mypage.dto.PublicRequestListDto;
import com.zipddak.mypage.dto.ReceiveRequestDetailDto;
import com.zipddak.mypage.dto.ReceiveRequestListDto;
import com.zipddak.mypage.repository.RequestDslRepository;
import com.zipddak.repository.ExpertRepository;
import com.zipddak.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpertRequestServiceImpl implements ExpertRequestService {

	private final RequestDslRepository requestDslRepository;
	private final ExpertRepository expertRepository;

	// 공개 요청서 목록 조회
	@Override
	public List<PublicRequestListDto> getPublicRequestList(Long lastId, int size) throws Exception {
		return requestDslRepository.selectPublicRequestList(lastId, size);
	}

	// 공개 요청서 상세 조회
	@Override
	public PublicRequestDetailDto getPublicRequestDetail(Integer requestIdx) throws Exception {
		return requestDslRepository.selectPublicRequestDetail(requestIdx);
	}

	// [전문가]받은 요청서 목록 조회
	@Override
	public List<ReceiveRequestListDto> getExpertReceiveRequestList(String username, PageInfo pageInfo)
			throws Exception {
		// 전문가 조회
		Expert expert = expertRepository.findByUser_Username(username).get();

		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 9);

		List<ReceiveRequestListDto> requestList = requestDslRepository.selectReceiveRequestList(expert.getExpertIdx(),
				pageRequest);
		Long cnt = requestDslRepository.selectReceiveRequestCount(expert.getExpertIdx());

		Integer allPage = (int) (Math.ceil(cnt.doubleValue() / pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage() - 1) / 9 * 9 + 1;
		Integer endPage = Math.min(startPage + 9 - 1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);

		return requestList;
	}

	// [전문가]받은 요청서 상세 조회
	@Override
	public ReceiveRequestDetailDto getExpertReceiveRequestDetail(Integer requestIdx) throws Exception {
		return requestDslRepository.selectReceiveRequestDetail(requestIdx);
	}
}
