package com.zipddak.user.service;

import com.zipddak.dto.UserDto;
import com.zipddak.entity.User;

public interface UserService {
	
	//로그인
	UserDto login(User user) throws Exception;

	// 전문가 여부 확인
	Boolean checkExpert(String username) throws Exception;

	// 주소 불러오기
	Boolean checkAddr(String username) throws Exception;
	
	// 유저정보 불러오기

}
