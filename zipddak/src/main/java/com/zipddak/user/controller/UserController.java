package com.zipddak.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.user.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping(value="/checkDoubleId")
	public ResponseEntity<Boolean> checkDoubleId (@RequestParam("username") String username){
		try {
			Boolean checkId = userService.checkDoubleId(username);
			return ResponseEntity.ok().body(checkId);
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(false);
		}
	}
	
}
