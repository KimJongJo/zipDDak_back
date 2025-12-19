package com.zipddak.user.service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.zipddak.dto.RentalDto;
import com.zipddak.entity.Rental;
import com.zipddak.repository.RentalRepository;

@Service
public class RentalServiceImpl implements RentalService {
	
	@Autowired
	private RentalRepository rentalRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	//대여 등록
	@Override
	@Transactional
	public void rentalApplication(RentalDto rentalDto, String orderId) throws Exception {
		System.out.println("rental service");
		
		Rental rental = modelMapper.map(rentalDto, Rental.class);
		rental.setRentalCode(orderId);
		
		rentalRepository.save(rental);
	}



}
