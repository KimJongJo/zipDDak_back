package com.zipddak.user.service;

public interface UserService {

	// 전문가 여부 확인
	Boolean checkExpert(String username) throws Exception;

	// 주소 불러오기
	Boolean checkAddr(String username) throws Exception;
	// 유저정보 불러오기

}
