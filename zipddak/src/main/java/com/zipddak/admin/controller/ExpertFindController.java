package com.zipddak.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.admin.dto.ExpertCardDto;
import com.zipddak.admin.dto.ExpertCareerDto;
import com.zipddak.admin.dto.ExpertPortfolioDto;
import com.zipddak.admin.dto.ExpertProfileDto;
import com.zipddak.admin.dto.ExpertsMainListsDto;
import com.zipddak.admin.dto.ResponseExpertProfileDto;
import com.zipddak.admin.service.CategoryService;
import com.zipddak.admin.service.ExpertCareerService;
import com.zipddak.admin.service.ExpertFindService;
import com.zipddak.admin.service.PortfolioService;
import com.zipddak.dto.CategoryDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ExpertFindController {

	private final ExpertFindService expertFindService;
	private final CategoryService categoryService;
	private final ExpertCareerService expertCareerService;
	private final PortfolioService portFolioService;
	
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
	public ResponseEntity<ResponseExpertProfileDto> expertInfo(@RequestParam("expertIdx") Integer expertIdx){
		
		try {
			ExpertProfileDto expertProfile = expertFindService.expertProfile(expertIdx);
			
			// 이거는 제공 서비스가 존재 할때
			String providedServiceIdxs = expertProfile.getProvidedServiceIdx();
			List<CategoryDto> categoryList = categoryService.providedService(providedServiceIdxs);
			
			// 커리어
			ExpertCareerDto careerDto = expertCareerService.expertCareer(expertIdx);
			
			// 포트폴리오
			List<ExpertPortfolioDto> portFolioDtoList = portFolioService.expertPortfolio(expertIdx);
			
			ResponseExpertProfileDto response = new ResponseExpertProfileDto(expertProfile, 
																		categoryList,
																		careerDto,
																		portFolioDtoList);
			
			return ResponseEntity.ok(response);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
}
