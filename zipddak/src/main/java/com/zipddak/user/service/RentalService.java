package com.zipddak.user.service;

import com.zipddak.dto.RentalDto;

public interface RentalService {
	
	//대여 등록
	void rentalApplication (RentalDto rentalDto, String orderId) throws Exception;

}
