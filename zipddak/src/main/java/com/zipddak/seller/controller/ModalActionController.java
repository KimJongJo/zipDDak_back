package com.zipddak.seller.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.seller.dto.OrderItemActionRequestDto;
import com.zipddak.seller.dto.SaveResultDto;
import com.zipddak.seller.service.ModalActionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ModalActionController {
	
	private final ModalActionService modal_svc;
	
	//운송장 등록
	@PostMapping("/registerTrackingNo")
	public ResponseEntity<?> registerTrackingNo(@RequestBody OrderItemActionRequestDto reqItems) {
		System.out.println("reqItems : "  + reqItems);
		
		SaveResultDto result = modal_svc.registerTrackingNo(reqItems);

		if (!result.isSuccess()) { //운송장 등록 실패한 경우 
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);

	}

}
