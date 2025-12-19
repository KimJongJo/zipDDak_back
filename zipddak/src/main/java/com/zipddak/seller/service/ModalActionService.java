package com.zipddak.seller.service;

import com.zipddak.seller.dto.OrderItemActionRequestDto;
import com.zipddak.seller.dto.SaveResultDto;

public interface ModalActionService {
	
	
	//운송장 등록
	SaveResultDto registerTrackingNo(OrderItemActionRequestDto reqItems);

}
