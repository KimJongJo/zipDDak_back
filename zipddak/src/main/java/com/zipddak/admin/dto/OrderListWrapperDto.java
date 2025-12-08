package com.zipddak.admin.dto;

import java.util.List;


import lombok.Data;

@Data
public class OrderListWrapperDto {

	private List<OrderListDto> orderList;
	private String username;
	
}
