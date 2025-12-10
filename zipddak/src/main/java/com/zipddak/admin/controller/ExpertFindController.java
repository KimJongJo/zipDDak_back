package com.zipddak.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.admin.dto.ExpertCardDto;
import com.zipddak.admin.dto.ExpertsMainListsDto;
import com.zipddak.admin.service.ExpertFindService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ExpertFindController {

	private final ExpertFindService expertFindService;
	
	@GetMapping("experts")
	public ResponseEntity<ExpertsMainListsDto> experts() {
		
		try {
			
			// 광고 전문가
			List<ExpertCardDto> addExperts = expertFindService.addExperts();
			// 일반 전문가							1 -> page	23 -> 수리 카테고리
			List<ExpertCardDto> experts = expertFindService.experts(1, 23, null, "popular");
			
			ExpertsMainListsDto expertListDto = new ExpertsMainListsDto(addExperts, experts);
			
			return ResponseEntity.ok(expertListDto);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
}
