package com.zipddak.admin.service;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zipddak.admin.dto.SettlementsTargetListDto;
import com.zipddak.admin.repository.PaymentDslRepository;
import com.zipddak.entity.Settlement;
import com.zipddak.mypage.repository.SettlementDslRepository;
import com.zipddak.repository.SettlementRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminSettlementServiceImpl implements SettlementService{

	private final PaymentDslRepository paymentDslRepository;
	private final SettlementRepository settlementRepository;
	
	@Override
	public void createMonthlySettlement() throws Exception {
		
		// 전월 기준
        YearMonth targetMonth = YearMonth.now().minusMonths(1);
        log.info("정산 대상 월: {}", targetMonth);
        
        List<SettlementsTargetListDto> targetList = paymentDslRepository.settlementsTarget(targetMonth);
        
        for(SettlementsTargetListDto target : targetList) {
//        	
//        	Settlement settlement = Settlement.builder()
//        							.
        	
        }
		
	}

}
