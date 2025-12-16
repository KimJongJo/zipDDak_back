package com.zipddak.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.user.dto.ToolCardsDto;
import com.zipddak.user.service.ToolService;

@RestController
public class ToolController {
	
	@Autowired
	private ToolService toolService;
	
	
	@GetMapping(value="/tool/mian")
	ResponseEntity<ToolCardsDto> toolList (
			@RequestParam(value="keyword", required=false, defaultValue = "") String keyword,
			@RequestParam("categoryNo") String categoryNo,			
			@RequestParam(value="username", required=false, defaultValue = "") String username,
			@RequestParam("wayNo") Integer wayNo,
			@RequestParam("orderNo") Integer orderNo,
			@RequestParam("rentalState") Boolean rentalState){
		
		try {
			ToolCardsDto toolList = toolService.toolCardsToolMain(categoryNo, keyword, username, wayNo, orderNo, rentalState);
			return ResponseEntity.ok(toolList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
		
		
	}

}
