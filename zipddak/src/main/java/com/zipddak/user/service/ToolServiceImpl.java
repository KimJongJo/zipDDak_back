package com.zipddak.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zipddak.user.dto.ToolCardsDto;
import com.zipddak.user.dto.ToolCardsMoreDto;
import com.zipddak.user.repository.ToolCardDsl;

@Service
public class ToolServiceImpl implements ToolService {
	
	@Autowired
	private ToolCardDsl toolCardDsl;

	//공구 메인
	@Override
	public ToolCardsMoreDto toolCardsToolMain(String categoryNo, String keyword, String username, Integer wayNo,
			Integer orderNo, Boolean rentalState, Integer offset, Integer size) throws Exception {
		ToolCardsDto toolCards = toolCardDsl.toolsToolMain(categoryNo, keyword, username, wayNo, orderNo, rentalState, offset,size);

		boolean hasNext = (offset + 1) * size < toolCards.getTotalCount();
		
		return new ToolCardsMoreDto(toolCards.getCards(),toolCards.getTotalCount(),hasNext);
	}

}
