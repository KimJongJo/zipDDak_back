package com.zipddak.user.service;

import com.zipddak.user.dto.ToolCardsDto;

public interface ToolService {
	
	//공구 리스트
	ToolCardsDto toolCardsToolMain (String categoryNo, String keyword, String username,
			Integer wayNo, Integer orderNo, Boolean rentalState)throws Exception;

}
