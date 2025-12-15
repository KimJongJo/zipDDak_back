package com.zipddak.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.entity.Matching.MatchingStatus;
import com.zipddak.entity.QCategory;
import com.zipddak.entity.QExpert;
import com.zipddak.entity.QMatching;
import com.zipddak.entity.QPayment;
import com.zipddak.entity.QRequest;
import com.zipddak.entity.QUser;
import com.zipddak.mypage.dto.MatchingListDto;
import com.zipddak.mypage.dto.MatchingStatusSummaryDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MatchingDslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	// [전문가]매칭 목록 조회
	public List<MatchingListDto> selectExpertMatchingList(Integer expertIdx, MatchingStatus status,
			PageRequest pageRequest, Date startDate, Date endDate) throws Exception {
		QMatching matching = QMatching.matching;
		QRequest request = QRequest.request;
		QExpert expert = QExpert.expert;
		QUser user = QUser.user;
		QCategory category = QCategory.category;
		QPayment payment = QPayment.payment;

		BooleanBuilder builder = new BooleanBuilder();

		// 전문가 아이디 조건
		builder.and(matching.expertIdx.eq(expertIdx));

		// 날짜 조건
		if (startDate != null) {
			builder.and(matching.createdAt.goe(startDate));
		}
		if (endDate != null) {
			builder.and(matching.createdAt.loe(endDate));
		}

		// 상태 조건
		if (status != null) {
			builder.and(matching.status.eq(status));
		}

		return jpaQueryFactory
				.select(Projections.constructor(MatchingListDto.class, matching.matchingIdx, matching.matchingCode,
						matching.createdAt, matching.workStartDate, matching.workEndDate, matching.status,
						payment.totalAmount, user.name, expert.activityName, request.largeServiceIdx, category.name,
						request.location, request.budget, request.preferredDate))
				.from(matching).leftJoin(request).on(request.requestIdx.eq(matching.requestIdx)).leftJoin(expert)
				.on(expert.expertIdx.eq(matching.expertIdx)).leftJoin(user).on(user.username.eq(matching.userUsername))
				.leftJoin(category).on(category.categoryIdx.eq(request.smallServiceIdx)).leftJoin(payment)
				.on(payment.paymentIdx.eq(matching.paymentIdx)).where(builder).offset(pageRequest.getOffset())
				.limit(pageRequest.getPageSize()).fetch();
	}

	// [전문가]매칭 목록 개수
	public Long selectExpertMatchingCount(Integer expertIdx, MatchingStatus status, Date startDate, Date endDate)
			throws Exception {
		QMatching matching = QMatching.matching;

		BooleanBuilder builder = new BooleanBuilder();

		// 전문가 아이디 조건
		builder.and(matching.expertIdx.eq(expertIdx));

		// 날짜 조건
		if (startDate != null) {
			builder.and(matching.createdAt.goe(startDate));
		}
		if (endDate != null) {
			builder.and(matching.createdAt.loe(endDate));
		}

		// 상태 조건
		if (status != null) {
			builder.and(matching.status.eq(status));
		}

		return jpaQueryFactory.select(matching.count()).from(matching).where(builder).fetchOne();
	}

	// [전문가]매칭현황 요약
	public MatchingStatusSummaryDto selectMatchingStatusSummary(Integer expertIdx) throws Exception {
		QMatching matching = QMatching.matching;

		return jpaQueryFactory
				.select(Projections.constructor(MatchingStatusSummaryDto.class,
						matching.status.when(MatchingStatus.PAYMENT_COMPLETED).then(1).otherwise(0).sum(),
						matching.status.when(MatchingStatus.IN_PROGRESS).then(1).otherwise(0).sum(),
						matching.status.when(MatchingStatus.COMPLETED).then(1).otherwise(0).sum(),
						matching.status.when(MatchingStatus.CANCELLED).then(1).otherwise(0).sum()))
				.from(matching).where(matching.expertIdx.eq(expertIdx)).fetchOne();
	}

}
