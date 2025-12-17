package com.zipddak.admin.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zipddak.admin.dto.AdminUserListDto;
import com.zipddak.admin.dto.RequestExpertInfoDto;
import com.zipddak.admin.dto.ResponseAdminListDto;
import com.zipddak.admin.repository.AdminDslRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
	
	private final AdminDslRepository adminDslRepository;
	
	// 회원 목록
	@Override
	public ResponseAdminListDto userList(Integer state, Integer column, String keyword, Integer page)
			throws Exception {

		return adminDslRepository.userList(state, column, keyword, page); 
	}

	// 전문가 목록
	@Override
	public ResponseAdminListDto expertList(Integer major, Integer state, Integer column, String keyword, Integer page)
			throws Exception {

		return adminDslRepository.expertList(major, state, column, keyword, page); 
	}

	// 판매업체 목록
	@Override
	public ResponseAdminListDto sellerList(Integer productCode, Integer state, String keyword, Integer page)
			throws Exception {

		return adminDslRepository.sellerList(productCode, state, keyword, page);
	}

	// 대여 내역
	@Override
	public ResponseAdminListDto rentalList(Integer column, Integer state, String keyword, Integer page, String startDate,
			String endDate) throws Exception {
		
		return adminDslRepository.rentalList(column, state, keyword, page, startDate, endDate);
	}

	// 판매 내역
	@Override
	public ResponseAdminListDto saleList(Integer column, Integer state, String keyword, Integer page, String startDate,
			String endDate) throws Exception {
		
		return adminDslRepository.saleList(column, state, keyword, page, startDate, endDate);
	}

	// 결제 내역
	@Override
	public ResponseAdminListDto paymentList(Integer type, Integer state, String keyword, Integer page)
			throws Exception {
		
		return adminDslRepository.paymentList(type, state, keyword, page);
	}

	// 멤버십 내역
	@Override
	public ResponseAdminListDto membershipList(Integer state, String keyword, Integer page) throws Exception {

		return adminDslRepository.membershipList(state, keyword, page);
	}

	// 전문가 전환 요청 목록
	@Override
	public ResponseAdminListDto requestExpertList(Integer state, Integer column, String keyword, Integer page) throws Exception {
		
		return adminDslRepository.requestExpertList(state, column, keyword, page);
	}

	// 판매업체 입점 신청 목록
	@Override
	public ResponseAdminListDto requestSellerList(Integer state, Integer column, String keyword, Integer page)
			throws Exception {
		
		return adminDslRepository.requestSellerList(state, column, keyword, page);
	}

	// 전문가 상세
	@Override
	public RequestExpertInfoDto requestExpertInfo(Integer expertIdx) throws Exception {

		RequestExpertInfoDto expertInfo = adminDslRepository.expertInfo(expertIdx);
		
		// 서비스 idx가 , 기준으로 나열되어있음
		String serviceString = expertInfo.getServiceString();
		
		List<Integer> serviceIdx = Arrays.stream(serviceString.split(","))
									.map(Integer::parseInt)
									.collect(Collectors.toList());
		
		List<String> service = new ArrayList<>();
		
		for(Integer categoryIdx : serviceIdx) {
			
			service.add(adminDslRepository.expertService(categoryIdx));
			
		}
		
		expertInfo.setService(service);
		
		return expertInfo;
	}

}

