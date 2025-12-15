package com.zipddak.user.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.entity.QCategory;
import com.zipddak.entity.QFavoritesTool;
import com.zipddak.entity.QReviewTool;
import com.zipddak.entity.QTool;
import com.zipddak.entity.QToolFile;
import com.zipddak.entity.Tool.ToolStatus;
import com.zipddak.user.dto.ToolCardDto;

@Repository
public class ToolCardDsl {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	public List<ToolCardDto> toolsMain(Integer categoryNo, String keyword, String username) {

		QCategory category = QCategory.category;
		QTool tool = QTool.tool;
		QToolFile toolFile = QToolFile.toolFile;
		QFavoritesTool favorite = QFavoritesTool.favoritesTool;
		QReviewTool review = QReviewTool.reviewTool;
		// 문의수가 matching인가...?

		// 로그인 했을때 안했을때 구분
		Expression<Boolean> isFavoriteTool = Expressions.asBoolean(false);

		if (username != null && !username.isBlank()) {
			isFavoriteTool = new CaseBuilder().when(favorite.toolIdx.isNotNull()).then(true).otherwise(false)
					.as("favorite");
		}

		BooleanBuilder where = new BooleanBuilder();

		// 공구 상태가 delete가 아닐때
		where.and(tool.satus.ne(ToolStatus.DELETE));

		// 카테고리
		where.and(tool.category.eq(categoryNo));
		
		//키워드
		if(keyword != null && keyword.isBlank()) {
			where.and(tool.name.contains(keyword));
		}
		
		//평점 높은순
		OrderSpecifier<?> order;
		order = review.score.avg().coalesce(0.0).desc();
		
		JPQLQuery<ToolCardDto> query = jpaQueryFactory.select(Projections.bean(ToolCardDto.class,
				tool.toolIdx,
				tool.name,
				tool.rentalPrice,
				tool.addr1,
				tool.satus,
				toolFile.fileRename,
				toolFile.storagePath,
				isFavoriteTool
				
				)).from(tool)
				.leftJoin(toolFile).on(toolFile.toolFileIdx.eq(tool.thunbnail))
				.leftJoin(category).on(category.categoryIdx.eq(tool.category));
		
		if(username != null && !username.isBlank()) {
			query.leftJoin(favorite)
					.on(favorite.toolIdx.eq(tool.toolIdx).and(favorite.userUsername.eq(username)));
		}
		
				
		return query.where(where)
				.groupBy(tool.toolIdx, tool.name, tool.rentalPrice,tool.addr1,tool.satus,
						toolFile.fileRename, toolFile.storagePath)
				.orderBy(order).limit(6).fetch();

	}

}
