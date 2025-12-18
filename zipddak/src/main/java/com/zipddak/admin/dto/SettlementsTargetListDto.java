package com.zipddak.admin.dto;

import com.zipddak.entity.Payment.PaymentType;
import com.zipddak.entity.User.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementsTargetListDto {

	private UserRole role;
	private String username;
	private PaymentType paymentType;
	private long totalCount;
	private Integer totalAmount;
}
