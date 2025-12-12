package com.zipddak.seller.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.zipddak.seller.dto.SaveResultDto;

@RestControllerAdvice
public class SellerController {

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<SaveResultDto> handleIllegalState(IllegalStateException ise) {
		return ResponseEntity.badRequest().body(new SaveResultDto(false, null, ise.getMessage()));
	}

}
