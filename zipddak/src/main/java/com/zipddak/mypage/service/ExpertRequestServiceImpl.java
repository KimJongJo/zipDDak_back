package com.zipddak.mypage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zipddak.mypage.dto.PublicRequestDetailDto;
import com.zipddak.mypage.dto.PublicRequestListDto;
import com.zipddak.mypage.repository.RequestDslRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpertRequestServiceImpl implements ExpertRequestService {

	private final RequestDslRepository requestDslRepository;

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
}
