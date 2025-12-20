package com.zipddak.user.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import com.zipddak.entity.QUser;
import com.zipddak.entity.Tool.ToolStatus;
import com.zipddak.user.dto.ToolCardDto;
import com.zipddak.user.dto.ToolCardsDto;
import com.zipddak.user.dto.ToolDetailDto;

@Repository
public class ToolCardDsl {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	//메인 공구카드
	public ToolCardsDto toolsMain(Integer categoryNo, String keyword, String username) {

		QCategory category = QCategory.category;
		QTool tool = QTool.tool;
		QToolFile toolFile = QToolFile.toolFile;
		QFavoritesTool favorite = QFavoritesTool.favoritesTool;
		QReviewTool review = QReviewTool.reviewTool;

		// 로그인 했을때 안했을때 구분
		Expression<Boolean> isFavoriteTool = Expressions.asBoolean(false).as("favorite");				
					
		if (username != null && !username.isBlank()) {
			isFavoriteTool = new CaseBuilder().when(favorite.toolIdx.isNotNull()).then(true).otherwise(false)
					.as("favorite");
		}

		BooleanBuilder where = new BooleanBuilder();

		// 대여 가능만 보기
		where.and(tool.satus.eq(ToolStatus.ABLE));

		// 카테고리
		where.and(tool.category.eq(categoryNo));
		
		//키워드
		if(keyword != null && !keyword.isBlank()) {
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
				.leftJoin(review).on(review.toolIdx.eq(tool.toolIdx))
				.leftJoin(category).on(category.categoryIdx.eq(tool.category));
		
		if(username != null && !username.isBlank()) {
			query.leftJoin(favorite)
					.on(favorite.toolIdx.eq(tool.toolIdx).and(favorite.userUsername.eq(username)));
		}
		 
			List<ToolCardDto> cards = query
					.where(where)
					.groupBy(tool.toolIdx)
					.orderBy(order).limit(6).fetch();

			Long totalCount = jpaQueryFactory
					.select(tool.toolIdx.countDistinct())
					.from(tool)
					.where(where)
					.fetchOne();

			return new ToolCardsDto(cards, totalCount); 

	}
	
	
	//공구메인
	public ToolCardsDto toolsToolMain (String categoryNo, String keyword, String username,
			Integer wayNo, Integer orderNo, Boolean rentalState,Integer offset, Integer size) {

		QCategory category = QCategory.category;
		QTool tool = QTool.tool;
		QToolFile toolFile = QToolFile.toolFile;
		QFavoritesTool favorite = QFavoritesTool.favoritesTool;
		QReviewTool review = QReviewTool.reviewTool;
		
		

		// 로그인 했을때 안했을때 구분
		Expression<Boolean> isFavoriteTool = Expressions.asBoolean(false).as("favorite");				
					
		if (username != null && !username.isBlank()) {
			isFavoriteTool = new CaseBuilder().when(favorite.toolIdx.isNotNull()).then(true).otherwise(false)
					.as("favorite");
		}

		BooleanBuilder where = new BooleanBuilder();

		Boolean includeRented = Boolean.FALSE.equals(rentalState);

		if (includeRented) {
			// 대여 가능만 보기
			where.and(tool.satus.eq(ToolStatus.ABLE));
		    
		} else {
		    // 대여중 보기
			where.and(tool.satus.eq(ToolStatus.INABLE));
		}

		// 카테고리
		if (categoryNo != null && !categoryNo.isBlank()) {
			List<Integer> categoryList = Arrays.stream(categoryNo.split(","))
		            .map(String::trim)
		            .map(Integer::parseInt)
		            .collect(Collectors.toList());
		    
		    where.and(tool.category.in(categoryList));
		}
		
		
		//키워드
		if(keyword != null && !keyword.isBlank()) {
			where.and(tool.name.contains(keyword));
		}
		
		//정렬기준
		List<OrderSpecifier<?>> orders = new ArrayList<>();

		switch (orderNo != null ? orderNo : 0) {
		
		case 1: // 평점 높은순
			orders.add(review.score.avg().coalesce(0.0).desc());
		    break;

		case 2: // 가격 낮은순
		    orders.add(tool.rentalPrice.asc());
		    break;

		case 3: // 가격 높은순
		    orders.add(tool.rentalPrice.desc());
		    break;

		default: // 평점 높은순
		    orders.add(review.score.avg().coalesce(0.0).desc());
		}

		orders.add(tool.toolIdx.desc());
		
		
		JPQLQuery<ToolCardDto> query = jpaQueryFactory.select(Projections.bean(ToolCardDto.class,
				tool.toolIdx,
				tool.name,
				tool.rentalPrice,
				tool.addr1,
				tool.satus,
				tool.directRental,
				tool.postRental,
				category.name.as("categoryName"),
				toolFile.fileRename,
				toolFile.storagePath,
				isFavoriteTool
				
				)).from(tool)
				.leftJoin(toolFile).on(toolFile.toolFileIdx.eq(tool.thunbnail))
				.leftJoin(review).on(review.toolIdx.eq(tool.toolIdx))
				.leftJoin(category).on(category.categoryIdx.eq(tool.category));
		
		if(username != null && !username.isBlank()) {
			query.leftJoin(favorite)
					.on(favorite.toolIdx.eq(tool.toolIdx).and(favorite.userUsername.eq(username)));
		}
		 
		if (!orders.isEmpty()) {
		    query.orderBy(orders.toArray(new OrderSpecifier[0]));
		}
		
		int limit = (size != null) ? size : 5;
		long realOffset = (offset != null) ? offset : 0;
		
		
			List<ToolCardDto> cards = query.where(where)
					.groupBy(tool.toolIdx)
//					.distinct()
					.limit(limit)
				    .offset(realOffset)
				    .fetch();
			

			Long totalCount = jpaQueryFactory
				    .select(tool.toolIdx.countDistinct())
				    .from(tool)
				    .where(where)
				    .fetchOne();
		

			return new ToolCardsDto(cards, totalCount); 

	}
	
	
	
	//내공구
	public ToolCardsDto myTools (String username,Integer rentalStateNo, Integer size, Integer offset) {

		QCategory category = QCategory.category;
		QTool tool = QTool.tool;
		QToolFile toolFile = QToolFile.toolFile;
		QFavoritesTool favorite = QFavoritesTool.favoritesTool;
		QReviewTool review = QReviewTool.reviewTool;
		

		// 로그인 했을때 안했을때 구분
		Expression<Boolean> isFavoriteTool = Expressions.asBoolean(false).as("favorite");				
					
		if (username != null && !username.isBlank()) {
			isFavoriteTool = new CaseBuilder().when(favorite.toolIdx.isNotNull()).then(true).otherwise(false)
					.as("favorite");
		}

		BooleanBuilder where = new BooleanBuilder();

		
		//대여 상태 
		switch (rentalStateNo) {
		
		case 1: 
			where.and(tool.satus.eq(ToolStatus.ABLE));
		    break;

		case 2: 
			where.and(tool.satus.eq(ToolStatus.INABLE));
		    break;
		    
		case 3:
			where.and(tool.satus.eq(ToolStatus.STOP));
			break;
		
		default:
			where.and(tool.satus.ne(ToolStatus.DELETE));
			break;
			
		}
		
		
		//정렬기준
		List<OrderSpecifier<?>> orders = new ArrayList<>();

		orders.add(tool.toolIdx.desc());
		
		
		JPQLQuery<ToolCardDto> query = jpaQueryFactory.select(Projections.bean(ToolCardDto.class,
				tool.toolIdx,
				tool.name,
				tool.rentalPrice,
				tool.satus,
				tool.directRental,
				tool.postRental,
				category.name.as("categoryName"),
				toolFile.fileRename,
				toolFile.storagePath,
				isFavoriteTool
				
				)).from(tool)
				.leftJoin(toolFile).on(toolFile.toolFileIdx.eq(tool.thunbnail))
				.leftJoin(review).on(review.toolIdx.eq(tool.toolIdx))
				.leftJoin(category).on(category.categoryIdx.eq(tool.category));
		
		if(username != null && !username.isBlank()) {
			query.leftJoin(favorite)
					.on(favorite.toolIdx.eq(tool.toolIdx).and(favorite.userUsername.eq(username)));
		}
		 
		if (!orders.isEmpty()) {
		    query.orderBy(orders.toArray(new OrderSpecifier[0]));
		}
		
		int limit = (size != null) ? size : 5;
		long realOffset = (offset != null) ? offset : 0;
		
		
			List<ToolCardDto> cards = query.where(where)
					.groupBy(tool.toolIdx)
//					.distinct()
					.limit(limit)
				    .offset(realOffset)
				    .fetch();
			

			Long totalCount = jpaQueryFactory
				    .select(tool.toolIdx.countDistinct())
				    .from(tool)
				    .where(where)
				    .fetchOne();
		

			return new ToolCardsDto(cards, totalCount); 

	}
	
	//공구 상세
	public ToolDetailDto toolDetails (Integer toolIdx, String username)throws Exception {
		
		QCategory category = QCategory.category;
		QTool tool = QTool.tool;
		QToolFile toolFile = QToolFile.toolFile;
		QFavoritesTool favorite = QFavoritesTool.favoritesTool;
		QReviewTool review = QReviewTool.reviewTool;
		QUser ownerUser = QUser.user;
		

		// 로그인 했을때 안했을때 구분
		Expression<Boolean> isFavoriteTool = Expressions.asBoolean(false).as("favorite");				
					
		if (username != null && !username.isBlank()) {
			isFavoriteTool = new CaseBuilder().when(favorite.toolIdx.isNotNull()).then(true).otherwise(false)
					.as("favorite");
		}

		
		JPQLQuery<ToolDetailDto> query = jpaQueryFactory.select(Projections.bean(ToolDetailDto.class,
				tool.toolIdx,
				tool.name,
				tool.rentalPrice,
				tool.freeRental,
				tool.content,
				tool.tradeAddr,
				tool.quickRental,
				tool.directRental,
				tool.postRental,
				tool.postCharge,
				tool.zonecode,
				tool.addr1,
				tool.addr2,
				tool.postRequest,
				tool.satus,
				tool.owner,
				tool.createdate,
				tool.thunbnail,
				tool.img1,
				tool.img2,
				tool.img3,
				tool.img4,
				tool.img5,
				tool.settleBank,
				tool.settleAccount,
				tool.settleHost,
				ownerUser.nickname,
				category.name.as("categoryName"),
				toolFile.fileRename,
				toolFile.storagePath,
				isFavoriteTool
				
				)).from(tool)
				.leftJoin(toolFile).on(toolFile.toolFileIdx.eq(tool.thunbnail))
				.leftJoin(ownerUser).on(ownerUser.username.eq(tool.owner))
				.leftJoin(review).on(review.toolIdx.eq(tool.toolIdx))
				.leftJoin(category).on(category.categoryIdx.eq(tool.category));
		
		if(username != null && !username.isBlank()) {
			query.leftJoin(favorite)
					.on(favorite.toolIdx.eq(tool.toolIdx).and(favorite.userUsername.eq(username)));
		}
				
		
		ToolDetailDto toolDetail = query
			    .where(tool.toolIdx.eq(toolIdx))
			    .groupBy(tool.toolIdx)
			    .fetchOne();
		
		return toolDetail;
		
	}
	
	//유저의 다른 공구
	public ToolCardsDto toolOwner (String username, String owner, Integer toolIdx) {

		QCategory category = QCategory.category;
		QTool tool = QTool.tool;
		QToolFile toolFile = QToolFile.toolFile;
		QFavoritesTool favorite = QFavoritesTool.favoritesTool;
		QReviewTool review = QReviewTool.reviewTool;

		// 로그인 했을때 안했을때 구분
		Expression<Boolean> isFavoriteTool = Expressions.asBoolean(false).as("favorite");				
					
		if (username != null && !username.isBlank()) {
			isFavoriteTool = new CaseBuilder().when(favorite.toolIdx.isNotNull()).then(true).otherwise(false)
					.as("favorite");
		}

		// 대여 가능만 보기
//		where.and(tool.satus.eq(ToolStatus.ABLE));

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
				.leftJoin(review).on(review.toolIdx.eq(tool.toolIdx))
				.leftJoin(category).on(category.categoryIdx.eq(tool.category));
		
		if(username != null && !username.isBlank()) {
			query.leftJoin(favorite)
					.on(favorite.toolIdx.eq(tool.toolIdx).and(favorite.userUsername.eq(username)));
		}
		 
			List<ToolCardDto> cards = query
					.where(
							tool.owner.eq(owner),
							tool.toolIdx.ne(toolIdx)
							)
					.groupBy(tool.toolIdx)
					.orderBy(order).limit(6).fetch();

			Long totalCount = jpaQueryFactory
					.select(tool.toolIdx.countDistinct())
					.from(tool)
					.where(
							tool.owner.eq(owner),
							tool.toolIdx.ne(toolIdx)
							)
					.fetchOne();

			return new ToolCardsDto(cards, totalCount); 

	}
	


}
