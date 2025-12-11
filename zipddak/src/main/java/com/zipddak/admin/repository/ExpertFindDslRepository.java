package com.zipddak.admin.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.admin.dto.ExpertCardDto;
import com.zipddak.entity.QCareer;
import com.zipddak.entity.QCategory;
import com.zipddak.entity.QExpert;
import com.zipddak.entity.QExpertFile;
import com.zipddak.entity.QMatching;
import com.zipddak.entity.QReviewExpert;

@Repository
public class ExpertFindDslRepository {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	public List<ExpertCardDto> experts(PageRequest pageRequest, Integer categoryNo, String keyword, String sort) {
		
		QCategory category = QCategory.category;
		QExpert expert = QExpert.expert;
		QMatching matching = QMatching.matching;
		QReviewExpert review = QReviewExpert.reviewExpert;
		QCareer career = QCareer.career;
		QExpertFile file = QExpertFile.expertFile;
		
		// where절 만들기
		BooleanBuilder builder = new BooleanBuilder();
		if(keyword != null && !keyword.isEmpty()) {
		    BooleanBuilder keywordBuilder = new BooleanBuilder();
		    keywordBuilder.or(expert.activityName.contains(keyword));
		    keywordBuilder.or(category.name.contains(keyword));
		    keywordBuilder.or(expert.introduction.contains(keyword));

		    builder.and(keywordBuilder); // 모든 OR 조건을 AND로 묶음
		}
		
		// order절 만들기
		OrderSpecifier<?> order = null;
		
		switch(sort) {
		case "popular" : order = matching.expertIdx.count().desc(); break;
		case "rating" : order = review.score.avg().desc(); break;
		case "career" : order = Expressions.numberTemplate(Integer.class,
								"SUM(DATEDIFF({0}, {1}))",
								career.endDate,
								career.startDate).desc(); break;
		}
		
		OrderSpecifier<?> secondOrder = expert.expertIdx.desc();
		
		
		
		
		return jpaQueryFactory.select(Projections.bean(ExpertCardDto.class, 
				expert.expertIdx,
				expert.addr1,
				expert.addr2,
				file.fileRename.as("imgFileRename"),
				file.storagePath.as("imgStoragePath"),
				expert.activityName,
				expert.mainServiceIdx,
				category.name.as("mainServiceName"),
				Expressions.numberTemplate(Double.class,
											"COALESCE(ROUND({0}, 1), 0)",  // null이면 0으로 대체
											review.score.avg()).as("avgRating"),
				review.score.count().as("reviewCount"),
				expert.introduction,
				matching.expertIdx.count().as("matchingCount"),
				Expressions.numberTemplate(Integer.class,
						 			"COALESCE(SUM(DATEDIFF({0}, {1})), 0)",
										career.endDate,
										career.startDate).as("career")
						))
				.from(expert)
				.leftJoin(file).on(expert.profileImageIdx.eq(file.expertFileIdx))
				.leftJoin(category).on(expert.mainServiceIdx.eq(category.categoryIdx))
				.leftJoin(matching).on(expert.expertIdx.eq(matching.expertIdx))
				.leftJoin(review).on(expert.expertIdx.eq(review.expertIdx))
				.leftJoin(career).on(expert.expertIdx.eq(career.expertIdx))
				.where(builder)
				.groupBy(expert.expertIdx)
				.orderBy(order, secondOrder)
				.offset(pageRequest.getOffset())
				.limit(pageRequest.getPageSize())
				.fetch();
	}
	
	
	
}
