package com.zipddak.admin.service;

import java.util.List;

import com.zipddak.admin.dto.ExpertCardDto;

public interface ExpertFindService {

	List<ExpertCardDto> addExperts()throws Exception;

	List<ExpertCardDto> experts()throws Exception;

}
