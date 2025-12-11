package com.zipddak.user.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.auth.PrincipalDetails;
import com.zipddak.dto.UserDto;
import com.zipddak.entity.User;
import com.zipddak.user.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserService userService;
	
		@PostMapping("/login")
		public ResponseEntity<UserDto> userInfo (@AuthenticationPrincipal PrincipalDetails principalDetails,
				@RequestParam("fcmToken") String fcmToken) {
			try {
				User user = principalDetails.getUser();
				user.setFcmToken(fcmToken);
				UserDto userInfo = userService.login(user);
//				alarmService.registFcmToken(user.getUsername(), fcmToken);
				
				return ResponseEntity.ok(userInfo);
				
			}catch(Exception e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body(null);
			}
		}
	

}
