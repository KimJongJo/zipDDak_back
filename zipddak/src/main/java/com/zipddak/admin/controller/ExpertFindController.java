package com.zipddak.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public ResponseEntity<List<ExpertCardDto>> experts(
				@RequestParam("page") Integer page,
				@RequestParam("cateNo") Integer cateNo,
				@RequestParam(value = "keyword", required = false) String keyword,
				@RequestParam("sort") String sort
			) {
		
		try {
			List<ExpertCardDto> experts = expertFindService.experts(page, cateNo, keyword, sort);
			
//			ExpertsMainListsDto expertListDto = new ExpertsMainListsDto(addExperts, experts);
			
			return ResponseEntity.ok(experts);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	@GetMapping("addExperts")
	public ResponseEntity<List<ExpertCardDto>> addExperts(
				@RequestParam("cateNo") Integer cateNo
			) {
		
		try {
			
			// 광고 전문가
			List<ExpertCardDto> addExperts = expertFindService.addExperts(cateNo);
			
			return ResponseEntity.ok(addExperts);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	@GetMapping("expertProfile")
	public ResponseEntity<?> expertInfo(@RequestParam("expertIdx") Integer expertIdx){
		
		try {
//			expertFindService.expertProfile(expertIdx);
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
}
