package com.zipddak.admin.service;

import java.sql.Date;
import java.util.List;

import com.zipddak.admin.dto.AdminUserListDto;
import com.zipddak.admin.dto.RequestExpertInfoDto;
import com.zipddak.admin.dto.ResponseAdminListDto;

public interface AdminService {

	ResponseAdminListDto userList(Integer state, Integer column, String keyword, Integer page) throws Exception;

	ResponseAdminListDto expertList(Integer major, Integer state, Integer column, String keyword, Integer page) throws Exception;

	ResponseAdminListDto sellerList(Integer productCode, Integer state, String keyword, Integer page) throws Exception;

	ResponseAdminListDto rentalList(Integer column, Integer state, String keyword, Integer page, String startDate,
			String endDate) throws Exception;

	ResponseAdminListDto saleList(Integer column, Integer state, String keyword, Integer page, String startDate,
			String endDate) throws Exception;

	ResponseAdminListDto paymentList(Integer type, Integer state, String keyword, Integer page) throws Exception;

	ResponseAdminListDto membershipList(Integer state, String keyword, Integer page) throws Exception;

	ResponseAdminListDto requestExpertList(Integer state, Integer column, String keyword, Integer page) throws Exception;

	ResponseAdminListDto requestSellerList(Integer state, Integer column, String keyword, Integer page) throws Exception;

	RequestExpertInfoDto requestExpertInfo(Integer expertIdx) throws Exception;

}
