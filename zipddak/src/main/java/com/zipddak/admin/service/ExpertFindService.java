package com.zipddak.admin.service;

import java.util.List;

import com.zipddak.admin.dto.ExpertCardDto;

public interface ExpertFindService {

	List<ExpertCardDto> addExperts()throws Exception;

	List<ExpertCardDto> experts(Integer page, Integer categoryNo, String keyword, String sort)throws Exception;

}
