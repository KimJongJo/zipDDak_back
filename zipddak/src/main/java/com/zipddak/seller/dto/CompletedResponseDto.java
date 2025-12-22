package com.zipddak.seller.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletedResponseDto {
	
	 private boolean success;
	 private LocalDate pickupCompletedAt;

}
