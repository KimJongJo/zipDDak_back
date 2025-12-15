package com.zipddak.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zipddak.admin.dto.ExpertCardDto;
import com.zipddak.admin.dto.ProductCardDto;
import com.zipddak.user.dto.ToolCardDto;
import com.zipddak.user.repository.ExpertCardDsl;
import com.zipddak.user.repository.ProductCardDsl;
import com.zipddak.user.repository.ToolCardDsl;

@Service
public class MainServiceImpl implements MainService {
	
	@Autowired
	private ExpertCardDsl expertCardDsl;
	
	@Autowired
	private ToolCardDsl toolCardDsl;
	
	@Autowired
	private ProductCardDsl productCardDsl;

	@Override
	public List<ExpertCardDto> expertCardMain(Integer categoryNo, String keyword) throws Exception {
		return expertCardDsl.expertsMain(categoryNo, keyword);
	}

	@Override
	public List<ToolCardDto> toolCardMain(Integer categoryNo, String keyword, String username) throws Exception {
		return toolCardDsl.toolsMain(categoryNo, keyword, username);
	}

	@Override
	public List<ProductCardDto> productCardMain(Integer categoryNo, String keyword, String username) throws Exception {
		return productCardDsl.productsMain(categoryNo, keyword, username);
	}

	
	
	
	
	@Override
	public List<ProductCardDto> products100(String username) throws Exception {
		return productCardDsl.Bestproducts(username);
	}

}
