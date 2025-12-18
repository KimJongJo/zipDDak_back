package com.zipddak.admin.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.admin.dto.SettlementsTargetListDto;
import com.zipddak.entity.QPayment;
import com.zipddak.entity.QUser;

@Repository
public class PaymentDslRepository {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	// 해당 년/월에 결제된 모든 결제 목록을 불러오기
	public List<SettlementsTargetListDto> settlementsTarget(YearMonth month) {

		QPayment payment = QPayment.payment;
		QUser user = QUser.user;
		
		LocalDateTime startLdt = month.atDay(1).atStartOfDay();
		LocalDateTime endLdt   = month.plusMonths(1).atDay(1).atStartOfDay();

		// 2️⃣ LocalDateTime → Timestamp 변환
		Timestamp start = Timestamp.valueOf(startLdt);
		Timestamp end   = Timestamp.valueOf(endLdt);
		
		return jpaQueryFactory.select(Projections.bean(SettlementsTargetListDto.class, 
					
					user.role,
					payment.username,
					payment.paymentType,
					payment.count().as("totalCount"),
					payment.totalAmount.sum().as("totalAmount")
				
				))
				.from(payment)
				.where(payment.approvedAt.goe(start),
						payment.approvedAt.lt(end),
						payment.status.eq("DONE")
					)
				.groupBy(payment.username, payment.paymentType)
				.fetch();
	}
	
}
