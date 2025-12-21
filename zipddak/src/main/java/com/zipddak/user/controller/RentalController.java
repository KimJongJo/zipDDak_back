package com.zipddak.user.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.dto.RentalDto;
import com.zipddak.user.service.RentalService;

@RestController
public class RentalController {
	
	@Autowired
	private RentalService rentalService;
	
	//대여등록
		@PostMapping(value="/rental/application")
		ResponseEntity<Boolean> toolRegist (@RequestBody RentalDto rentalDto) {
			try {
				
				// orderId 생성
				String orderId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
		                + "-" + (int)(Math.random() * 9000 + 1000);
				
				rentalService.rentalApplication(rentalDto,orderId);
				System.out.println("rental application controller");
				
				return ResponseEntity.ok(true);
			}catch(Exception e) {
				return ResponseEntity.badRequest().body(false);
			}
		}

}
