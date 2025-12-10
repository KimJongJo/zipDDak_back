package com.zipddak.mypage.service;

import java.util.List;

import com.zipddak.mypage.dto.PublicRequestDetailDto;
import com.zipddak.mypage.dto.PublicRequestListDto;

public interface ExpertRequestService {
	List<PublicRequestListDto> getPublicRequestList(Long lastId, int size) throws Exception;

	PublicRequestDetailDto getPublicRequestDetail(Integer requestIdx) throws Exception;
}
