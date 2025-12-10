package com.zipddak.mypage.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.mypage.dto.PublicRequestDetailDto;
import com.zipddak.mypage.dto.PublicRequestListDto;
import com.zipddak.mypage.service.ExpertRequestServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExpertRequestController {

	private final ExpertRequestServiceImpl requestService;

	// 공개 요청서 목록 조회
	@GetMapping("/publicRequestsList")
	public ResponseEntity<List<PublicRequestListDto>> publicRequestsList(@RequestParam(required = false) Long lastId,
			@RequestParam(defaultValue = "10") int size) {
		try {
			return ResponseEntity.ok(requestService.getPublicRequestList(lastId, size));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}

	// 공개 요청서 상세 조회
	@GetMapping("/publicRequestsDetail")
	public ResponseEntity<PublicRequestDetailDto> publicRequestsDetail(@RequestParam Integer requestIdx) {
		try {
			return ResponseEntity.ok(requestService.getPublicRequestDetail(requestIdx));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}
}
