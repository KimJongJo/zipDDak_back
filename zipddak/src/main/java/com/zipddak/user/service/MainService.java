package com.zipddak.user.service;

import java.util.List;

import com.zipddak.admin.dto.ExpertCardDto;
import com.zipddak.admin.dto.ProductCardDto;
import com.zipddak.user.dto.ToolCardDto;

public interface MainService {
	
	//전문가
	List<ExpertCardDto> expertCardMain(Integer categoryNo, String keyword) throws Exception;
	
	//공구
	List<ToolCardDto> toolCardMain(Integer categoryNo, String keyword, String username) throws Exception;
	
	//상품
	List<ProductCardDto> productCardMain(Integer categoryNo, String keyword, String username)throws Exception;
	
	//커뮤니티
	
	
	//베스트 100
	List<ProductCardDto> products100(String username)throws Exception;
}
