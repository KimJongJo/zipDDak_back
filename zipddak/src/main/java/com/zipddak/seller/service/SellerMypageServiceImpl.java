package com.zipddak.seller.service;

import org.springframework.stereotype.Service;

import com.zipddak.dto.SellerDto;
import com.zipddak.seller.exception.NotFoundException;
import com.zipddak.seller.repository.SellerMypageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerMypageServiceImpl implements SellerMypageService {
	
	private final SellerMypageRepository sellerMypage_repo;
	
	
	//프로필 상세보기
	@Override
	public SellerDto getMyProfileDetail(String sellerUsername) {
		//상품 정보(옵션 제외)
		
		SellerDto SellerDto = sellerMypage_repo.findBySellerIdxAndSellerId(sellerUsername);
		if (SellerDto == null) {
			throw new NotFoundException("판매자 정보 없음 또는 권한 없음");
	    }
		
		return SellerDto;
	}

}
