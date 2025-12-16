package com.zipddak.admin.service;

import java.sql.Date;
import java.util.List;

import com.zipddak.admin.dto.AdminUserListDto;
import com.zipddak.admin.dto.ResponseAdminListDto;

public interface AdminService {

	ResponseAdminListDto userList(Integer state, Integer column, String keyword, Integer page) throws Exception;

	ResponseAdminListDto expertList(Integer major, Integer state, Integer column, String keyword, Integer page) throws Exception;

	ResponseAdminListDto sellerList(Integer productCode, Integer state, String keyword, Integer page) throws Exception;

	ResponseAdminListDto rentalList(Integer column, Integer state, String keyword, Integer page, Date startDate,
			Date endDate) throws Exception;

}
