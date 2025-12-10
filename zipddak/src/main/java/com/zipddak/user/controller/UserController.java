package com.zipddak.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	@PostMapping(value = "/user")
	public ResponseEntity<Boolean> checkDoubleId() {
		try {
			
			return ResponseEntity.ok().body(true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(false);
		}
	}
	

}
