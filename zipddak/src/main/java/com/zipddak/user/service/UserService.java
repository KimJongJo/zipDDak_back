package com.zipddak.user.service;

import com.zipddak.entity.User;
import com.zipddak.user.dto.UserInfoDto;

public interface UserService {
	
	//로그인
	UserInfoDto login(User user) throws Exception;

	// 전문가 전환
	UserInfoDto expertYN(Boolean isExpert, String username) throws Exception;

	// 주소 불러오기
	Boolean checkAddr(String username) throws Exception;
	
	// 유저정보 불러오기

}
