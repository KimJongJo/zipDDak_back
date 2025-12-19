package com.zipddak.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.dto.RentalDto;
import com.zipddak.user.service.RentalService;

@RestController
public class RentalController {
	
	@Autowired
	private RentalService rentalService;
	
	//대여등록
//		@PostMapping(value="/rental/application")
//		ResponseEntity<Integer> toolRegist (@RequestBody RentalDto rentalDto) {
//			try {
//				Integer rentalIdx = rentalService.rentalApplication(rentalDto);
//				System.out.println("rental application controller");
//				return ResponseEntity.ok(rentalIdx);
//			}catch(Exception e) {
//				return ResponseEntity.badRequest().body(null);
//			}
//		}

}
