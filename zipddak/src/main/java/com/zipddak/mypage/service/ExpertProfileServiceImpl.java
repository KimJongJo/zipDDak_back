package com.zipddak.mypage.service;

import org.springframework.stereotype.Service;

import com.zipddak.dto.ExpertDto;
import com.zipddak.entity.Expert;
import com.zipddak.mypage.repository.ExpertDslRepository;
import com.zipddak.repository.ExpertRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpertProfileServiceImpl implements ExpertProfileService {

	private final ExpertRepository expertRepository;
	private final ExpertDslRepository expertDslRepository;

	// 전문가 상세 조회
	@Override
	public ExpertDto getExpertDetail(String username) throws Exception {
		Expert expert = expertRepository.findByUser_Username(username).get();

		return expert.toDto();
	}
}
