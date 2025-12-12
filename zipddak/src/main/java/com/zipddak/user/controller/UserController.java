package com.zipddak.user.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.auth.PrincipalDetails;
import com.zipddak.entity.User;
import com.zipddak.user.dto.UserInfoDto;
import com.zipddak.user.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserService userService;
	
		//로그인
		@PostMapping("/zipddak")
		public ResponseEntity<UserInfoDto> userInfo (@AuthenticationPrincipal PrincipalDetails principalDetails,
				@RequestParam("fcmToken") String fcmToken) {
			try {
				User user = principalDetails.getUser();
				user.setFcmToken(fcmToken);
				UserInfoDto userInfo = userService.login(user);
				userInfo.setExpert(false);
//				alarmService.registFcmToken(user.getUsername(), fcmToken);
				
				return ResponseEntity.ok(userInfo);
				
			}catch(Exception e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body(null);
			}
		}
		
		@GetMapping("/expertYn")
		public ResponseEntity<UserInfoDto> expertYn (@RequestParam("isExpert") Boolean isExpert, @RequestParam("username") String username){
			try {
				
				UserInfoDto userInfo = userService.expertYN(isExpert, username);
						
				return ResponseEntity.ok(userInfo);
			}catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body(null);
			}
		}
	

}
