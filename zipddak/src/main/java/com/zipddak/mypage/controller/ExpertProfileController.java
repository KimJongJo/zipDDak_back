package com.zipddak.mypage.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zipddak.dto.CareerDto;
import com.zipddak.dto.ExpertDto;
import com.zipddak.dto.InquiriesDto;
import com.zipddak.dto.PortfolioDto;
import com.zipddak.mypage.dto.ExpertProfileDto;
import com.zipddak.mypage.service.ExpertProfileServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExpertProfileController {

	private final ExpertProfileServiceImpl expertProfileService;

	// 전문가 상세 조회
	@GetMapping("/profile/detail")
	public ResponseEntity<ExpertProfileDto> ExpertDetail(@RequestParam String username) {
		try {
			System.out.print(expertProfileService.getExpertProfileDetail(username));
			return ResponseEntity.ok(expertProfileService.getExpertProfileDetail(username));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// 전문가 프로필 수정
		@PostMapping("/profile/modify")
		public ResponseEntity<Boolean> modifyExpertProfile(ExpertDto expertDto,
				@RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
				@RequestParam(value = "businessImage", required = false) MultipartFile businessImage,
				@RequestParam(value = "certificateImages", required = false) MultipartFile[] certificateImages) {
			try {
				expertProfileService.modifyExpertProfile(expertDto, profileImage, businessImage,certificateImages);
				return ResponseEntity.ok(true);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body(null);
			}
		}
	
	// 포트폴리오 추가
	@PostMapping("/portfolio/write")
	public ResponseEntity<String> writePortfolio(PortfolioDto portfolioDto,
			@RequestParam(value = "portfolioImages", required = false) MultipartFile[] portfolioImages) {
		try {
			String img1Rename = expertProfileService.addPortfolio(portfolioDto, portfolioImages);
			return ResponseEntity.ok(img1Rename);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// 포트폴리오 삭제
		@PostMapping("/portfolio/delete")
		public ResponseEntity<Boolean> deletePortfolio(@RequestBody Map<String, Integer> req) {
			try {
				expertProfileService.deletePortfolio(req.get("portfolioIdx"));
				return ResponseEntity.ok(true);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body(null);
			}
		}
	
	// 경력 추가
	@PostMapping("/career/write")
	public ResponseEntity<Boolean> writeCareer(CareerDto careerDto) {
		try {
			expertProfileService.addCareer(careerDto);
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// 경력 삭제
	@PostMapping("/career/delete")
	public ResponseEntity<Boolean> deleteCareer(@RequestBody Map<String, Integer> req) {
		try {
			expertProfileService.deleteCareer(req.get("careerIdx"));
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}
}
