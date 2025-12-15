package com.zipddak.mypage.service;

import java.util.List;

import com.zipddak.mypage.dto.PublicRequestDetailDto;
import com.zipddak.mypage.dto.PublicRequestListDto;
import com.zipddak.mypage.dto.ReceiveRequestDetailDto;
import com.zipddak.mypage.dto.ReceiveRequestListDto;
import com.zipddak.util.PageInfo;

public interface ExpertRequestService {
	List<PublicRequestListDto> getPublicRequestList(Long lastId, int size) throws Exception;

	PublicRequestDetailDto getPublicRequestDetail(Integer requestIdx) throws Exception;
	
	List<ReceiveRequestListDto> getExpertReceiveRequestList(String username, PageInfo pageInfo) throws Exception;
	
	ReceiveRequestDetailDto getExpertReceiveRequestDetail(Integer requestIdx) throws Exception;
}
