package com.zipddak.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.zipddak.dto.CategoryDto;
import com.zipddak.dto.UserDto;
import com.zipddak.entity.Category.CategoryType;
import com.zipddak.user.dto.ExpertInsertDto;

public interface SignUpService {
	
	//유저 회원가입
	void joinUser (UserDto userDto)throws Exception;
	void joinExpert (ExpertInsertDto expertDto, MultipartFile file)throws Exception;
	
	//중복 아이디 체크
	Boolean checkDoubleId (String username) throws Exception;
	
	//전문가 카테고리 불러오기
	Map<Integer, List<CategoryDto>> showExpertCategory (List<Integer> parentIdxList, CategoryType  type) throws Exception;
	
	//로그인
	UserDto login (String username, String password) throws Exception;

	//휴대폰 인증
	
	
	
	
}
