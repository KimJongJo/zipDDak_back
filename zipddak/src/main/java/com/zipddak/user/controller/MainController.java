package com.zipddak.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.admin.dto.ExpertCardDto;
import com.zipddak.admin.dto.ProductCardDto;
import com.zipddak.user.dto.ToolCardDto;
import com.zipddak.user.service.MainService;

@RestController
public class MainController {
	
	@Autowired
	private MainService mainService;
	
	//전문가 리스트
	@GetMapping(value="/main/expert")
	ResponseEntity<List<ExpertCardDto>> mainExpertList (@RequestParam("categoryNo") Integer categoryNo,
			@RequestParam("keyword") String keyword){
		
		try {
			List<ExpertCardDto> expertMain = mainService.expertCardMain(categoryNo, keyword);
			return ResponseEntity.ok(expertMain);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	//공구 리스트
	@GetMapping(value="/main/tool")
	ResponseEntity<List<ToolCardDto>> mainToolList (
			@RequestParam("categoryNo") Integer categoryNo,
			@RequestParam("keyword") String keyword,
			@RequestParam("username")String username) {
		
		try {
			List<ToolCardDto> toolMain = mainService.toolCardMain(categoryNo, keyword, username);
			return ResponseEntity.ok(toolMain);
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	//상품 리스트
		@GetMapping(value="/main/product")
		ResponseEntity<List<ProductCardDto>> mainProductList (
				@RequestParam("categoryNo") Integer categoryNo,
				@RequestParam("keyword") String keyword,
				@RequestParam("username")String username) {
			
			try {
				List<ProductCardDto> productMain = mainService.productCardMain(categoryNo, keyword, username);
				return ResponseEntity.ok(productMain);
			}catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body(null);
			}
			
		}
		
	//커뮤니티 리스트
		
	//상품 베스트100
		@GetMapping(value="/main/best")
		ResponseEntity<List<ProductCardDto>> bestProductList (
				@RequestParam("username")String username) {
			
			try {
				List<ProductCardDto> productMain = mainService.products100(username);
				return ResponseEntity.ok(productMain);
			}catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body(null);
			}
			
		}
		
	
}
