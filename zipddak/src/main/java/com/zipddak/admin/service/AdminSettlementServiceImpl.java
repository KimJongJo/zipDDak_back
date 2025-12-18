package com.zipddak.admin.service;

import java.sql.Date;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zipddak.admin.dto.SettlementsTargetListDto;
import com.zipddak.admin.repository.PaymentDslRepository;
import com.zipddak.dto.SettlementSellerTargetListDto;
import com.zipddak.entity.Settlement;
import com.zipddak.entity.Settlement.SettlementState;
import com.zipddak.entity.Settlement.TargetType;
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
	@Transactional
	public void createMonthlySettlement() throws Exception {
		
		// 전월 기준
        YearMonth targetMonth = YearMonth.now().minusMonths(1);
        log.info("정산 대상 월: {}", targetMonth);
        
        // 전문가 정산 리스트
        List<SettlementsTargetListDto> expertSettlementList = paymentDslRepository.expertSettlementsTarget(targetMonth);
        
        // 판매업체 정산 리스트
        List<SettlementSellerTargetListDto> sellerSettlementList = paymentDslRepository.sellerSettlementsTarget(targetMonth);
        
        
        Date targetDate = Date.valueOf(targetMonth.atDay(1));
        
        // 전문가 정산 리스트를 통해서 정산 테이블에 저장
        // 이미 정산되어있는 정산 리스트면 저장 제외
        for(SettlementsTargetListDto target : expertSettlementList) {
        	
        	Optional<Settlement> targetSettlement = settlementRepository.findByTargetUsernameAndTargetTypeAndSettlementMonth(
        			target.getUsername(), TargetType.EXPERT,  targetDate
        			);
        	
        	// 존재하지 않는다면 새로 만들어야함
        	if(!targetSettlement.isPresent()) {
        		
        		Settlement settlement = Settlement.builder()
        								.amount(target.getTotalAmount())
        								.state(SettlementState.PENDING)
        								.targetUsername(target.getUsername())
        								.feeRate(5)
        								.settlementAmount(
        									    target.getTotalAmount() * 95 / 100
        									)
        								.targetType(TargetType.EXPERT)
        								.settlementMonth(targetDate)
        								.build();
        		
        		settlementRepository.save(settlement);
        	}
        	
        }
        
        // 판매업체 정산 리스트를 통해서 정산 테이블에 저장
        for(SettlementSellerTargetListDto target : sellerSettlementList) {
        	
        	Optional<Settlement> targetSettlement = settlementRepository.findByTargetUsernameAndTargetTypeAndSettlementMonth(
        			target.getUsername(), TargetType.SELLER,  targetDate
        			);
        	
        	// 존재하지 않는다면 새로 만들어야함
        	if(!targetSettlement.isPresent()) {
        		
        		Settlement settlement = Settlement.builder()
        								.amount((int)target.getTotalAmount())
        								.state(SettlementState.PENDING)
        								.targetUsername(target.getUsername())
        								.feeRate(5)
        								.settlementAmount(
        									    ((int)target.getTotalAmount()) * 95 / 100
        									)
        								.targetType(TargetType.SELLER)
        								.settlementMonth(targetDate)
        								.build();
        		
        		settlementRepository.save(settlement);
        	}
        	
        }
		
	}

}
