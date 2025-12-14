package com.zipddak.mypage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.mypage.dto.EstimateWriteDto;
import com.zipddak.mypage.service.EstimateServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EstimateController {

	private final EstimateServiceImpl estimateService;

	// 견적서 보내기
	@PostMapping("/estimate/write")
	public ResponseEntity<Boolean> writeEstimate(@RequestBody EstimateWriteDto EstimateWriteDto) {
		try {
			estimateService.writeEstimate(EstimateWriteDto);
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}

}
