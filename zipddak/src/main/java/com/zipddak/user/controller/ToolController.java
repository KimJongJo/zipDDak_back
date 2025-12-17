package com.zipddak.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.user.dto.ToolCardsMoreDto;
import com.zipddak.user.service.ToolService;

@RestController
public class ToolController {
	
	@Autowired
	private ToolService toolService;
	
	
	@GetMapping(value="/tool/main")
	ResponseEntity<ToolCardsMoreDto> toolList (
			@RequestParam(value="keyword", required=false, defaultValue = "") String keyword,
			@RequestParam(value="categoryNo", required=false, defaultValue = "") String categoryNo,			
			@RequestParam(value="username", required=false, defaultValue = "") String username,
			@RequestParam(value="wayNo",required=false) Integer wayNo,
			@RequestParam(value="orderNo",required=false) Integer orderNo,
			@RequestParam(value="rentalState",required=false) Boolean rentalState,
			@RequestParam(value="offset", defaultValue = "0") Integer offset,
			@RequestParam(value="size") Integer size){
		
		try {
			ToolCardsMoreDto toolList = toolService.toolCardsToolMain(categoryNo, keyword, username, wayNo, orderNo, rentalState, offset, size);
			return ResponseEntity.ok(toolList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
		
		
	}

}
