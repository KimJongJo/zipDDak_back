package com.zipddak.mypage.service;

import java.sql.Date;
import java.util.List;

import com.zipddak.mypage.dto.OrderListDto;
import com.zipddak.util.PageInfo;

public interface OrderService {
	List<OrderListDto> getOrderList(String username, PageInfo pageInfo, Date startDate, Date endDate) throws Exception;
}
