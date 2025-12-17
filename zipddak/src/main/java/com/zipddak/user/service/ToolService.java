package com.zipddak.user.service;

import com.zipddak.user.dto.ToolCardsMoreDto;

public interface ToolService {
	
	//공구 리스트
	ToolCardsMoreDto toolCardsToolMain (String categoryNo, String keyword, String username,
			Integer wayNo, Integer orderNo, Boolean rentalState,Integer offset, Integer size)throws Exception;

}
