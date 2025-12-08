package com.zipddak.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zipddak.dto.CategoryDto;
import com.zipddak.dto.UserDto;
import com.zipddak.entity.Category.CategoryType;
import com.zipddak.user.dto.ExpertInsertDto;
import com.zipddak.user.service.SignUpService;

@RestController
public class SignUpController {

	@Autowired
	private SignUpService signUpService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostMapping(value = "/checkDoubleId")
	public ResponseEntity<Boolean> checkDoubleId(@RequestBody Map<String, String> params) {
		try {
			String username = params.get("username");
			Boolean checkId = signUpService.checkDoubleId(username);
			return ResponseEntity.ok().body(checkId);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(false);
		}
	}

	@PostMapping(value = "/joinUser")
	public ResponseEntity<Boolean> joinUser(@RequestBody UserDto userDto) {
		try {
			String password = bCryptPasswordEncoder.encode(userDto.getPassword());
			userDto.setPassword(password);
			signUpService.joinUser(userDto);

			return ResponseEntity.ok(true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(false);
		}
	}

	@GetMapping(value = "/signUpExpertCategory")
	public ResponseEntity<Map<Integer, List<CategoryDto>>> signUpExpertCategory(
			@RequestParam("parentIdx") List<Integer> parentIdx, @RequestParam("type") CategoryType type) {

		Map<Integer, List<CategoryDto>> categoryList = new HashMap<>();
		System.out.println("타입>>"+type);
		System.out.println("dddd>>>>" + parentIdx);

		try {
			categoryList = signUpService.showExpertCategory(parentIdx, type);
			return ResponseEntity.ok(categoryList);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}

	@PostMapping(value = "/joinExpert")
	public ResponseEntity<Boolean> joinExpert(@RequestPart("businessLicenseFile") MultipartFile file,
			@RequestPart("expert") ExpertInsertDto expertDto) {
		try {
			System.out.println("보자>>>>>>>>"+expertDto);
			signUpService.joinExpert(expertDto,file);
			
			System.out.println("한번더>>>>>>>>"+expertDto);
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(false);
		}
	}

}
