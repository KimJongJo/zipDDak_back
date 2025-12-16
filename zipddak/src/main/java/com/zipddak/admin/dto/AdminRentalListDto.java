package com.zipddak.admin.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRentalListDto {

	private Integer rentalIdx;
	private String toolName;
	private String owner;
	private String borrower;
	private Date startDate;
	private Date endDate;
	private String state;
	
}
