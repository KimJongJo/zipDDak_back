package com.zipddak.mypage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.dto.ExpertDto;
import com.zipddak.mypage.service.ExpertProfileServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExpertProfileController {

	private final ExpertProfileServiceImpl expertProfileService;

	// 전문가 상세 조회
	@GetMapping("/profile/detail")
	public ResponseEntity<ExpertDto> ExpertDetail(@RequestParam String username) {
		try {
			return ResponseEntity.ok(expertProfileService.getExpertDetail(username));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}

}
