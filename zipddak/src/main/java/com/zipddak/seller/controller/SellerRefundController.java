package com.zipddak.seller.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zipddak.seller.dto.OrderItemActionRequestDto;
import com.zipddak.seller.dto.SaveResultDto;
import com.zipddak.seller.dto.SearchConditionDto;
import com.zipddak.seller.service.SellerOrderService;
import com.zipddak.seller.service.SellerRefundService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/refund")
@RequiredArgsConstructor
public class SellerRefundController {
	
	private final SellerRefundService refund_svc;
	
	// 반품 리스트
	@GetMapping("/myRefundList")
	public ResponseEntity<?> refundList(@RequestParam("sellerId") String sellerUsername, 
										@RequestParam(value="page", required=false, defaultValue="1") Integer page,
										SearchConditionDto scDto) {
		

		try {
			Map<String, Object> myRefund = refund_svc.getMyRefundList(sellerUsername, page, scDto);
			return ResponseEntity.ok(myRefund);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	// 반품요청건 내역 상세보기
	@GetMapping("/refundReqDetail")
	public ResponseEntity<?> refundReqDetail(@RequestParam("sellerId") String sellerUsername,@RequestParam("num") Integer refundIdx) {
			Map<String, Object> refundReqDetail = refund_svc.getRefundReqDetail(sellerUsername, refundIdx);
			System.out.println("refundReqDetail : " + refundReqDetail);
			return ResponseEntity.ok(refundReqDetail);
	}

	
	
	//반품 거절 처리 
	@PostMapping("/refundRejectItems")
    public ResponseEntity<?> refundRejectItems(@RequestBody OrderItemActionRequestDto reqItems) {
		System.out.println("reqItems : " + reqItems);
		
    	SaveResultDto result = refund_svc.refundRejectItems(reqItems);

		if (!result.isSuccess()) { //처리 실패한 경우 
			return ResponseEntity.badRequest().body(result);
		}
		return ResponseEntity.ok(result);
    }
	
	
	//반품 접수 수락 처리 
	@PostMapping("/refundAcceptItems")
    public ResponseEntity<?> refundAcceptItems(@RequestBody OrderItemActionRequestDto reqItems) {
		System.out.println("reqItems : " + reqItems);
		
    	SaveResultDto result = refund_svc.refundAcceptItems(reqItems);

		if (!result.isSuccess()) { //처리 실패한 경우 
			return ResponseEntity.badRequest().body(result);
		}
		return ResponseEntity.ok(result);
    }
		
		
		
	//환불처리 
	@PostMapping("/refundItems")
    public ResponseEntity<?> refundItems(@RequestBody OrderItemActionRequestDto reqItems) {
		System.out.println("reqItems : " + reqItems);
        try {
        	SaveResultDto result = refund_svc.refundItems(reqItems.getOrderIdx(),reqItems.getItemIdxs());

			 if (!result.isSuccess()) { //환불처리 실패한 경우 
		            return ResponseEntity.badRequest().body(result);
		        }
		        return ResponseEntity.ok(result);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
    }
}
