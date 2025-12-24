package com.zipddak.seller.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.dto.SellerDto;
import com.zipddak.seller.service.SellerMypageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/seller/mypage")
@RequiredArgsConstructor
public class SellerMypageController {
	
	private final SellerMypageService mypage_svc;
	
	
	//프로필 상세보기
	@GetMapping("/myProfile")
	public ResponseEntity<?> getMyProfileDetail(@RequestParam("sellerId") String sellerUsername) {
			SellerDto myProfileDetail = mypage_svc.getMyProfileDetail(sellerUsername);
			System.out.println("myProfileDetail" + myProfileDetail);
			
			return ResponseEntity.ok(myProfileDetail);
        
    }

}
