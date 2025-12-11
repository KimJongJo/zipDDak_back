package com.zipddak.admin.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
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
		QExpertFile file = QExpertFile.expertFile;
		QMatching matching = QMatching.matching;
		QReviewExpert review = QReviewExpert.reviewExpert;
		QCareer career = QCareer.career;
		
		// career months 합계를 expert 기준으로 가져오는 Expression
		NumberExpression<Integer> careerSumExpr = Expressions.numberTemplate(Integer.class,
		    "(select coalesce(sum(c2.months), 0) from Career c2 where c2.expertIdx = {0})",
		    expert.expertIdx
		);
		
		BooleanBuilder builder = new BooleanBuilder();

		// 카테고리 필터
		switch(categoryNo) {
		    case 23 : builder.and(expert.mainServiceIdx.gt(22).and(expert.mainServiceIdx.lt(44))); break;
		    case 44 : builder.and(expert.mainServiceIdx.gt(43).and(expert.mainServiceIdx.lt(74))); break;
		    case 74 : builder.and(expert.mainServiceIdx.eq(74)); break;
		}
		
		// where절 만들기
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
				review.reviewExpertIdx.countDistinct().as("reviewCount"),
				expert.introduction,
				matching.matchingIdx.countDistinct().as("matchingCount"),
				careerSumExpr.as("career")
						))
				.from(expert)
				.leftJoin(file).on(expert.profileImageIdx.eq(file.expertFileIdx))
				.leftJoin(category).on(expert.mainServiceIdx.eq(category.categoryIdx))
				.leftJoin(matching).on(expert.expertIdx.eq(matching.expertIdx))
				.leftJoin(review).on(expert.expertIdx.eq(review.expertIdx))
				.leftJoin(career).on(expert.expertIdx.eq(career.expertIdx))
				.where(builder)
				.groupBy(expert.expertIdx, expert.addr1, expert.addr2, file.fileRename, file.storagePath, expert.activityName,
						expert.mainServiceIdx, category.name, expert.introduction)
				.orderBy(order, secondOrder)
				.offset(pageRequest.getOffset())
				.limit(pageRequest.getPageSize())
				.fetch();
		}

	public List<ExpertCardDto> addExperts(Integer categoryNo) {
	
		QCategory category = QCategory.category;
		QExpert expert = QExpert.expert;
		QExpertFile file = QExpertFile.expertFile;
		QMatching matching = QMatching.matching;
		QReviewExpert review = QReviewExpert.reviewExpert;
		QCareer career = QCareer.career;

		NumberExpression<Integer> careerSumExpr = Expressions.numberTemplate(Integer.class,
			    "(select coalesce(sum(c2.months), 0) from Career c2 where c2.expertIdx = {0})",
			    expert.expertIdx
			);
		
		BooleanBuilder builder = new BooleanBuilder();

		// 카테고리 필터
		switch(categoryNo) {
		    case 23 : builder.and(expert.mainServiceIdx.gt(22).and(expert.mainServiceIdx.lt(44))); break;
		    case 44 : builder.and(expert.mainServiceIdx.gt(43).and(expert.mainServiceIdx.lt(74))); break;
		    case 74 : builder.and(expert.mainServiceIdx.eq(74)); break;
		}
		
		// 멤버십 종료일도 넣기
//		builder.and(builder)
		
		
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
				review.reviewExpertIdx.countDistinct().as("reviewCount"),
				expert.introduction,
				matching.matchingIdx.countDistinct().as("matchingCount"),
				careerSumExpr.as("career")
						))
				.from(expert)
				.leftJoin(file).on(expert.profileImageIdx.eq(file.expertFileIdx))
				.leftJoin(category).on(expert.mainServiceIdx.eq(category.categoryIdx))
				.leftJoin(matching).on(expert.expertIdx.eq(matching.expertIdx))
				.leftJoin(review).on(expert.expertIdx.eq(review.expertIdx))
				.leftJoin(career).on(expert.expertIdx.eq(career.expertIdx))
				.where(builder)
				.groupBy(expert.expertIdx, expert.addr1, expert.addr2, file.fileRename, file.storagePath, expert.activityName,
						expert.mainServiceIdx, category.name, expert.introduction)
				.orderBy(Expressions.numberTemplate(Double.class, "RAND()").asc())
		        .limit(3)
				.fetch();
		
	}
	
	
	
}
