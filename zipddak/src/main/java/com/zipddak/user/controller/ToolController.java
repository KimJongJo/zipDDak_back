package com.zipddak.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zipddak.dto.ToolDto;
import com.zipddak.entity.Tool.ToolStatus;
import com.zipddak.user.dto.ToolCardsDto;
import com.zipddak.user.dto.ToolCardsMoreDto;
import com.zipddak.user.dto.ToolDetailviewDto;
import com.zipddak.user.dto.ToolReviewDto;
import com.zipddak.user.service.ToolService;

@RestController
public class ToolController {
	
	@Autowired
	private ToolService toolService;
	
	
	//공구 메인 리스트
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
	
	//공구등록
	@PostMapping(value="/tool/regist", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Integer> toolRegist (
			@RequestPart("tool") ToolDto toolDto,
			 @RequestPart(required=false) MultipartFile thumbnail,
			 @RequestPart(required=false) List<MultipartFile> images
			) {
		try {
			Integer toolIdx = toolService.ToolRegist(toolDto, thumbnail, images);
			System.out.println("ㅎㅎ");
			return ResponseEntity.ok(toolIdx);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	//공구수정
	@PostMapping(value="/tool/modify", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Boolean> toolModify (
			@RequestPart("tool") ToolDto toolDto,
			 @RequestPart(required=false) MultipartFile thumbnail,
			 @RequestPart(required=false) List<MultipartFile> images
			) {
		try {
			toolService.ToolModify(toolDto, thumbnail, images);
			System.out.println("ㅎㅎ");
			return ResponseEntity.ok().body(true);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(false);
		}
	}
	
	//내공구
	@GetMapping(value="tool/myTool")
	ResponseEntity<ToolCardsMoreDto> myToolList (
			@RequestParam(value="rentalState", defaultValue = "") Integer toolStatusNo,			
			@RequestParam(value="username", defaultValue = "") String username,
			@RequestParam(value="offset", defaultValue = "0") Integer offset,
			@RequestParam(value="size") Integer size){
		
		try {
			ToolCardsMoreDto toolList = toolService.myTools(username, toolStatusNo, size, offset);
			return ResponseEntity.ok(toolList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	//대여중지
	@PostMapping(value="tool/stop")
	ResponseEntity<ToolStatus> stopTool (@RequestBody Map<String,String> param){
		
		String username = param.get("username");
		Integer toolIdx = Integer.parseInt(param.get("toolIdx"));
		
		try {
			ToolStatus toolStatus = toolService.stopTool(username, toolIdx);
			return ResponseEntity.ok(toolStatus);
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	
	//공구 삭제
	@PostMapping(value="tool/delete")
	ResponseEntity<ToolStatus> deleteTool (@RequestBody Map<String,String> param){
		
		String username = param.get("username");
		Integer toolIdx = Integer.parseInt(param.get("toolIdx"));
		
		try {
			ToolStatus toolStatus = toolService.delteTool(username, toolIdx);
			return ResponseEntity.ok(toolStatus);
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	
	//공구 상세
	@GetMapping(value="tool/detail")
	ResponseEntity<ToolDetailviewDto> targetTool (@RequestParam("toolIdx") Integer toolIdx, @RequestParam("username")String username){
		
		System.out.println(">>>>>"+toolIdx);
		try {
			ToolDetailviewDto toolDto = toolService.targetTool(toolIdx,username);
			System.out.println("toolDetailController>>"+toolDto);
			return ResponseEntity.ok(toolDto);
			
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	//유저의 다른 공구
		@GetMapping(value="tool/owner")
		ResponseEntity<ToolCardsDto> ownersTool (@RequestParam("owner") String owner, 
				@RequestParam("username")String username,
				@RequestParam("toolIdx")Integer toolIdx){
			
			try {
				System.out.println("controller");
				ToolCardsDto toolDto = toolService.ownersTool(username, owner,toolIdx);
				return ResponseEntity.ok(toolDto);
				
			}catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body(null);
			}
		}
	
		//공구 리뷰
		@GetMapping(value="tool/review")
		ResponseEntity<Map<String,Object>> toolReview (
				@RequestParam(defaultValue = "0") Integer page,
				@RequestParam("toolIdx")Integer toolIdx,
				@RequestParam("orderNo")Integer orderNo){
			
			try {
				System.out.println("review: toolIdx>>"+toolIdx);
				Map<String,Object> toolDto = toolService.toolsReview(toolIdx, page,orderNo);
				return ResponseEntity.ok(toolDto);
				
			}catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body(null);
			}
		}
	
	
	
	
	
	
}
