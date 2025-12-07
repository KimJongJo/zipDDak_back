package com.zipddak.user.service;

import com.zipddak.dto.UserDto;

public interface UserService {
	void join (UserDto userDto)throws Exception;
	UserDto login (String username, String password) throws Exception;
	Boolean checkDoubleId (String username) throws Exception;
	//휴대폰 인증
	
	
	Boolean checkExpert (String username) throws Exception;
	//주소 불러오기
	Boolean checkAddr (String username) throws Exception;
	
}
