package com.zipddak.mypage.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.entity.QCategory;
import com.zipddak.entity.QEstimate;
import com.zipddak.entity.QExpertFile;
import com.zipddak.entity.QMatching;
import com.zipddak.entity.QProfileFile;
import com.zipddak.entity.QRequest;
import com.zipddak.entity.QUser;
import com.zipddak.mypage.dto.PublicRequestDetailDto;
import com.zipddak.mypage.dto.PublicRequestListDto;
import com.zipddak.mypage.dto.ReceiveRequestDetailDto;
import com.zipddak.mypage.dto.ReceiveRequestListDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RequestDslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	// 공개 요청서 목록 조회
	public List<PublicRequestListDto> selectPublicRequestList(Long lastId, int size) throws Exception {
		QRequest request = QRequest.request;
		QUser user = QUser.user;
		QEstimate estimate = QEstimate.estimate;
		QCategory category = QCategory.category;
		QProfileFile profileFile = QProfileFile.profileFile;

		return jpaQueryFactory
				.select(Projections.constructor(PublicRequestListDto.class, request.requestIdx, request.createdAt,
						user.name, profileFile.fileRename, estimate.count(), category.name, request.location,
						request.budget, request.preferredDate))
				.from(request).leftJoin(estimate).on(estimate.requestIdx.eq(request.requestIdx)).leftJoin(category)
				.on(request.largeServiceIdx.eq(74).and(category.categoryIdx.eq(request.largeServiceIdx))
						.or(request.largeServiceIdx.ne(74).and(category.categoryIdx.eq(request.smallServiceIdx))))
				.leftJoin(user).on(user.username.eq(request.userUsername)).leftJoin(profileFile)
				.on(profileFile.profileFileIdx.eq(user.profileImg))
				.where(request.status.eq("RECRUITING"),
						lastId != null && lastId > 0 ? request.requestIdx.lt(lastId) : null)
				.groupBy(request.requestIdx, request.createdAt, user.name, category.name, request.location,
						request.budget, request.preferredDate)
				.orderBy(request.requestIdx.desc()).limit(size).fetch();
	}

	// 공개 요청서 상세 조회
	public PublicRequestDetailDto selectPublicRequestDetail(Integer requestIdx) throws Exception {
		QRequest request = QRequest.request;
		QUser user = QUser.user;
		QEstimate estimate = QEstimate.estimate;
		QMatching matching = QMatching.matching;
		QCategory category = QCategory.category;
		QProfileFile profileFile = QProfileFile.profileFile;
		QExpertFile expertFile1 = new QExpertFile("expertFile1");
		QExpertFile expertFile2 = new QExpertFile("expertFile2");
		QExpertFile expertFile3 = new QExpertFile("expertFile3");

		return jpaQueryFactory
				.select(Projections.constructor(PublicRequestDetailDto.class, request.requestIdx, request.createdAt,
						estimate.count(), user.name, profileFile.fileRename, matching.count(), request.largeServiceIdx,
						request.midServiceIdx, request.smallServiceIdx, category.name, request.location, request.budget,
						request.preferredDate, request.constructionSize, request.additionalRequest,
						expertFile1.fileRename, expertFile2.fileRename, expertFile3.fileRename, request.purpose,
						request.place))
				.from(request).leftJoin(estimate).on(estimate.requestIdx.eq(request.requestIdx)).leftJoin(user)
				.on(user.username.eq(request.userUsername)).leftJoin(matching)
				.on(matching.userUsername.eq(user.username)).leftJoin(category)
				.on(request.largeServiceIdx.eq(74).and(category.categoryIdx.eq(request.largeServiceIdx))
						.or(request.largeServiceIdx.ne(74).and(category.categoryIdx.eq(request.smallServiceIdx))))
				.leftJoin(expertFile1).on(expertFile1.expertFileIdx.eq(request.image1Idx)).leftJoin(expertFile2)
				.on(expertFile2.expertFileIdx.eq(request.image2Idx)).leftJoin(expertFile3)
				.on(expertFile3.expertFileIdx.eq(request.image3Idx)).leftJoin(profileFile)
				.on(profileFile.profileFileIdx.eq(user.profileImg)).where(request.requestIdx.eq(requestIdx))
				.groupBy(request.requestIdx, request.createdAt, user.name, profileFile.fileRename, category.name,
						request.location, request.budget, request.preferredDate, request.constructionSize,
						request.additionalRequest, expertFile1.fileRename, expertFile2.fileRename,
						expertFile3.fileRename, request.purpose, request.place)

				.fetchOne();
	}

	// [전문가]받은 요청서 목록
	public List<ReceiveRequestListDto> selectReceiveRequestList(Integer expertIdx, PageRequest pageRequest)
			throws Exception {
		QRequest request = QRequest.request;
		QCategory category = QCategory.category;

		return jpaQueryFactory
				.select(Projections.constructor(ReceiveRequestListDto.class, request.requestIdx, request.createdAt,
						category.name, request.location, request.budget, request.preferredDate))
				.from(request).leftJoin(category).on(category.categoryIdx.eq(request.smallServiceIdx))
				.where(request.expertIdx.eq(expertIdx).and(request.status.eq("RECRUITING")))
				.offset(pageRequest.getOffset()).limit(pageRequest.getPageSize()).fetch();
	}

	// [전문가]받은 요청서 개수
	public Long selectReceiveRequestCount(Integer expertIdx) throws Exception {
		QRequest request = QRequest.request;

		return jpaQueryFactory.select(request.count()).from(request)
				.where(request.expertIdx.eq(expertIdx).and(request.status.eq("RECRUITING"))).fetchOne();
	}

	// [전문가]받은 요청서 상세
	public ReceiveRequestDetailDto selectReceiveRequestDetail(Integer requestIdx) throws Exception {
		QRequest request = QRequest.request;
		QUser user = QUser.user;
		QCategory category = QCategory.category;
		QExpertFile expertFile1 = new QExpertFile("expertFile1");
		QExpertFile expertFile2 = new QExpertFile("expertFile2");
		QExpertFile expertFile3 = new QExpertFile("expertFile3");

		return jpaQueryFactory
				.select(Projections.constructor(ReceiveRequestDetailDto.class, request.requestIdx, request.createdAt,
						user.name, user.phone, request.largeServiceIdx, category.name, request.location, request.budget,
						request.preferredDate, request.constructionSize, request.additionalRequest, request.purpose,
						request.place, expertFile1.fileRename, expertFile2.fileRename, expertFile3.fileRename))
				.from(request).leftJoin(user).on(user.username.eq(request.userUsername)).leftJoin(category)
				.on(category.categoryIdx.eq(request.smallServiceIdx)).leftJoin(expertFile1)
				.on(expertFile1.expertFileIdx.eq(request.image1Idx)).leftJoin(expertFile2)
				.on(expertFile2.expertFileIdx.eq(request.image2Idx)).leftJoin(expertFile3)
				.on(expertFile3.expertFileIdx.eq(request.image3Idx)).where(request.requestIdx.eq(requestIdx))
				.fetchOne();
	}
}
