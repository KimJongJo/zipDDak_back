package com.zipddak.admin.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.admin.dto.AdminUserListDto;
import com.zipddak.admin.dto.ResponseAdminListDto;
import com.zipddak.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final AdminService adminService;

	@GetMapping("/users")
	public ResponseEntity<ResponseAdminListDto> users(@RequestParam Integer state,
									@RequestParam Integer column,
									@RequestParam String keyword,
									@RequestParam Integer page){
		
		try {
			
			ResponseAdminListDto userList = adminService.userList(state, column, keyword, page);
			
			return ResponseEntity.ok(userList);
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	@GetMapping("/experts")
	public ResponseEntity<ResponseAdminListDto> experts(@RequestParam Integer major,
									@RequestParam Integer state,
									@RequestParam Integer column,
									@RequestParam String keyword,
									@RequestParam Integer page){
		
		try {
			
			ResponseAdminListDto expertList = adminService.expertList(major, state, column, keyword, page);
			
			return ResponseEntity.ok(expertList);
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	@GetMapping("/sellers")
	public ResponseEntity<ResponseAdminListDto> sellers(@RequestParam Integer productCode,
									@RequestParam Integer state,
									@RequestParam String keyword,
									@RequestParam Integer page){
		
		
		try {
			
			ResponseAdminListDto sellerList = adminService.sellerList(productCode, state, keyword, page);
			
			return ResponseEntity.ok(sellerList);
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	@GetMapping("/rentals")
	public ResponseEntity<ResponseAdminListDto> renstals(@RequestParam Integer column,
									@RequestParam Integer state,
									@RequestParam String keyword,
									@RequestParam Integer page,
									@RequestParam(value = "startDate", required = false) String startDate,
									@RequestParam(value = "endDate", required = false) String endDate){
		
		
		try {
			
			ResponseAdminListDto rentalList = adminService.rentalList(column, state, keyword, page, startDate, endDate);
			
			return ResponseEntity.ok(rentalList);
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	@GetMapping("/sales")
	public ResponseEntity<ResponseAdminListDto> sales(@RequestParam Integer column,
									@RequestParam Integer state,
									@RequestParam String keyword,
									@RequestParam Integer page,
									@RequestParam(value = "startDate", required = false) String startDate,
									@RequestParam(value = "endDate", required = false) String endDate){
		
		
		try {
			
			ResponseAdminListDto rentalList = adminService.saleList(column, state, keyword, page, startDate, endDate);
			
			return ResponseEntity.ok(rentalList);
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	@GetMapping("/payments")
	public ResponseEntity<ResponseAdminListDto> payments(@RequestParam Integer type,
									@RequestParam Integer state,
									@RequestParam String keyword,
									@RequestParam Integer page){
		
		try {
			
			ResponseAdminListDto paymentList = adminService.paymentList(type, state, keyword, page);
			
			return ResponseEntity.ok(paymentList);
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
	
	
	
}
