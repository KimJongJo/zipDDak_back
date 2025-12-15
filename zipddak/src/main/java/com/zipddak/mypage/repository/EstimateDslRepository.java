package com.zipddak.mypage.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.entity.QCategory;
import com.zipddak.entity.QEstimate;
import com.zipddak.entity.QEstimateCost;
import com.zipddak.entity.QRequest;
import com.zipddak.mypage.dto.SentEstimateListDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EstimateDslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	// [전문가]보낸 견적서 목록 - 진행중인 견적 요청
	public List<SentEstimateListDto> selectProgressSentEstimateList(Integer expertIdx, PageRequest pageRequest)
			throws Exception {

		QEstimate estimate = QEstimate.estimate;
		QRequest request = QRequest.request;
		QEstimateCost estimateCost = QEstimateCost.estimateCost;
		QCategory category = QCategory.category;

		return jpaQueryFactory
				.select(Projections.constructor(SentEstimateListDto.class, estimate.estimateIdx, estimate.createdAt,
						estimateCost.amount.sum().coalesce(0).add(estimate.disposalCost.coalesce(0))
								.add(estimate.demolitionCost.coalesce(0)).add(estimate.etcFee.coalesce(0))
								.add(estimate.consultingLaborCost.coalesce(0))
								.add(estimate.stylingDesignCost.coalesce(0)).add(estimate.threeDImageCost.coalesce(0))
								.add(estimate.reportProductionCost.coalesce(0)),
						category.name, request.location, request.budget, request.preferredDate))
				.from(estimate).join(request).on(request.requestIdx.eq(estimate.requestIdx)).leftJoin(estimateCost)
				.on(estimateCost.estimateIdx.eq(estimate.estimateIdx)).leftJoin(category)
				.on(category.categoryIdx.eq(request.smallServiceIdx))
				.where(estimate.expert.expertIdx.eq(expertIdx).and(request.status.eq("RECRUITING")))
				.groupBy(estimate.estimateIdx, estimate.createdAt, request.status, estimate.largeServiceIdx,
						request.location, request.budget, request.preferredDate, estimate.disposalCost,
						estimate.demolitionCost, estimate.etcFee, estimate.consultingLaborCost,
						estimate.stylingDesignCost, estimate.threeDImageCost, estimate.reportProductionCost)
				.orderBy(estimate.createdAt.desc()).offset(pageRequest.getOffset()).limit(pageRequest.getPageSize())
				.fetch();
	}

	// [전문가]보낸 견적서 개수 - 진행중인 견적 요청
	public Long selectProgressSentEstimateCount(Integer expertIdx) throws Exception {
		QEstimate estimate = QEstimate.estimate;
		QRequest request = QRequest.request;

		return jpaQueryFactory.select(estimate.count()).from(estimate).join(request)
				.on(request.requestIdx.eq(estimate.requestIdx))
				.where(estimate.expert.expertIdx.eq(expertIdx).and(request.status.eq("RECRUITING"))).fetchOne();
	}

	// [전문가]보낸 견적서 목록 - 종료된 견적 요청
	public List<SentEstimateListDto> selectFinishSentEstimateList(Integer expertIdx, PageRequest pageRequest)
			throws Exception {

		QEstimate estimate = QEstimate.estimate;
		QRequest request = QRequest.request;
		QEstimateCost estimateCost = QEstimateCost.estimateCost;
		QCategory category = QCategory.category;

		return jpaQueryFactory
				.select(Projections.constructor(SentEstimateListDto.class, estimate.estimateIdx, estimate.createdAt,
						estimateCost.amount.sum().coalesce(0).add(estimate.disposalCost.coalesce(0))
								.add(estimate.demolitionCost.coalesce(0)).add(estimate.etcFee.coalesce(0))
								.add(estimate.consultingLaborCost.coalesce(0))
								.add(estimate.stylingDesignCost.coalesce(0)).add(estimate.threeDImageCost.coalesce(0))
								.add(estimate.reportProductionCost.coalesce(0)),
						category.name, request.location, request.budget, request.preferredDate))
				.from(estimate).join(request).on(request.requestIdx.eq(estimate.requestIdx)).leftJoin(estimateCost)
				.on(estimateCost.estimateIdx.eq(estimate.estimateIdx)).leftJoin(category)
				.on(category.categoryIdx.eq(request.smallServiceIdx))
				.where(estimate.expert.expertIdx.eq(expertIdx).and(request.status.ne("RECRUITING")))
				.groupBy(estimate.estimateIdx, estimate.createdAt, request.status, estimate.largeServiceIdx,
						request.location, request.budget, request.preferredDate, estimate.disposalCost,
						estimate.demolitionCost, estimate.etcFee, estimate.consultingLaborCost,
						estimate.stylingDesignCost, estimate.threeDImageCost, estimate.reportProductionCost)
				.orderBy(estimate.createdAt.desc()).offset(pageRequest.getOffset()).limit(pageRequest.getPageSize())
				.fetch();
	}

	// [전문가]보낸 견적서 개수 - 종료된 견적 요청
	public Long selectFinishSentEstimateCount(Integer expertIdx) throws Exception {
		QEstimate estimate = QEstimate.estimate;
		QRequest request = QRequest.request;

		return jpaQueryFactory.select(estimate.count()).from(estimate).join(request)
				.on(request.requestIdx.eq(estimate.requestIdx))
				.where(estimate.expert.expertIdx.eq(expertIdx).and(request.status.ne("RECRUITING"))).fetchOne();
	}

}
