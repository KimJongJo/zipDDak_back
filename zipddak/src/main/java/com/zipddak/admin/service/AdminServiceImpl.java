package com.zipddak.admin.service;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zipddak.admin.dto.AdminUserListDto;
import com.zipddak.admin.dto.ResponseAdminListDto;
import com.zipddak.admin.repository.AdminDslRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
	
	private final AdminDslRepository adminDslRepository;
	
	@Override
	public ResponseAdminListDto userList(Integer state, Integer column, String keyword, Integer page)
			throws Exception {

		return adminDslRepository.userList(state, column, keyword, page); 
	}

	@Override
	public ResponseAdminListDto expertList(Integer major, Integer state, Integer column, String keyword, Integer page)
			throws Exception {

		return adminDslRepository.expertList(major, state, column, keyword, page); 
	}

	@Override
	public ResponseAdminListDto sellerList(Integer productCode, Integer state, String keyword, Integer page)
			throws Exception {

		return adminDslRepository.sellerList(productCode, state, keyword, page);
	}

	@Override
	public ResponseAdminListDto rentalList(Integer column, Integer state, String keyword, Integer page, String startDate,
			String endDate) throws Exception {
		
		return adminDslRepository.rentalList(column, state, keyword, page, startDate, endDate);
	}

	@Override
	public ResponseAdminListDto saleList(Integer column, Integer state, String keyword, Integer page, String startDate,
			String endDate) throws Exception {
		
		return adminDslRepository.saleList(column, state, keyword, page, startDate, endDate);
	}

	@Override
	public ResponseAdminListDto paymentList(Integer type, Integer state, String keyword, Integer page)
			throws Exception {
		
		return adminDslRepository.paymentList(type, state, keyword, page);
	}

}

