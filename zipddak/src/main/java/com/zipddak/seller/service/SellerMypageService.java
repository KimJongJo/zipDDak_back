package com.zipddak.seller.service;

import com.zipddak.dto.SellerDto;

public interface SellerMypageService {

	//프로필 상세보기
	SellerDto getMyProfileDetail(String sellerUsername);

}
