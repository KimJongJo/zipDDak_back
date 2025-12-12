package com.zipddak.admin.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.zipddak.admin.dto.ExpertCardDto;
import com.zipddak.admin.dto.ExpertProfileDto;
import com.zipddak.admin.repository.ExpertFindDslRepository;
import com.zipddak.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpertFindServiceImpl implements ExpertFindService{
	
	private final ExpertFindDslRepository expertFindDslRepository;
	
	// 광고 전문가
	@Override
	public List<ExpertCardDto> addExperts(Integer categoryNo) throws Exception {
		
		return expertFindDslRepository.addExperts(categoryNo);
	}

	// 일반 전문가
	@Override
	public List<ExpertCardDto> experts(Integer page, Integer categoryNo, String keyword, String sort) throws Exception {
		PageInfo pageInfo = new PageInfo(page);
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 9);
		
		return expertFindDslRepository.experts(pageRequest, categoryNo, keyword, sort);
	}

	// 전문가 프로필 구하기
	@Override
	public ExpertProfileDto expertProfile(Integer expertIdx) throws Exception {
		
		return expertFindDslRepository.expertProfile(expertIdx); 
	}


}
