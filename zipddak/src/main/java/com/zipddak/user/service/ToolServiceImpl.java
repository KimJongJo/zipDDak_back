package com.zipddak.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zipddak.user.dto.ToolCardsDto;
import com.zipddak.user.repository.ToolCardDsl;

@Service
public class ToolServiceImpl implements ToolService {
	
	@Autowired
	private ToolCardDsl toolCardDsl;

	//공구 메인
	@Override
	public ToolCardsDto toolCardsToolMain(String categoryNo, String keyword, String username, Integer wayNo,
			Integer orderNo, Boolean rentalState) throws Exception {
		
		return toolCardDsl.toolsToolMain(categoryNo, keyword, username, wayNo, orderNo, rentalState);
	}

}
