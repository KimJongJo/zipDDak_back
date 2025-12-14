package com.zipddak.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zipddak.admin.service.CommunityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class CommunityController {
	
	private final CommunityService communityService;

	@PostMapping("user/writeCommunity")
	public ResponseEntity<Integer> writeCommunity(
					@RequestParam int category,
					@RequestParam String title,
					@RequestParam String content,
					@RequestParam String username,
					@RequestPart(required = false) List<MultipartFile> images
			) {
		
		try {
			
			Integer commutnityIdx = communityService.write(category, title, content, username, images);
			
			return ResponseEntity.ok(commutnityIdx);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
			
		}
		
	}
	
}
