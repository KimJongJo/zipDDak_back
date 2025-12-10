package com.zipddak.mypage.service;

import com.zipddak.dto.ExpertDto;

public interface ExpertProfileService {
	ExpertDto getExpertDetail(String username) throws Exception;
}
