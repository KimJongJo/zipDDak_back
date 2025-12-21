package com.zipddak.user.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import com.zipddak.entity.QProfileFile;
import com.zipddak.entity.QReviewFile;
import com.zipddak.entity.QReviewTool;
import com.zipddak.entity.QTool;
import com.zipddak.entity.QToolFile;
import com.zipddak.entity.QUser;
import com.zipddak.entity.Tool.ToolStatus;
import com.zipddak.user.dto.ToolCardDto;
import com.zipddak.user.dto.ToolCardsDto;
import com.zipddak.user.dto.ToolDetailviewDto;
import com.zipddak.user.dto.ToolReviewDto;

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
		if(categoryNo != null && categoryNo != 0) {
		where.and(tool.category.eq(categoryNo));
		}
		
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
				toolFile.fileRename.as("thunbnail"),
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
				toolFile.fileRename.as("thunbnail"),
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
		
		// 주인 조건
		where.and(tool.owner.eq(username));
		
		
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
				toolFile.fileRename.as("thunbnail"),
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
	public ToolDetailviewDto toolDetails (Integer toolIdx, String username)throws Exception {
		
		QCategory category = QCategory.category;
		QTool tool = QTool.tool;
		QFavoritesTool favorite = QFavoritesTool.favoritesTool;
//		QReviewTool review = QReviewTool.reviewTool;
		QUser ownerUser = QUser.user;
		QProfileFile ownerProfile = QProfileFile.profileFile;
		
		QToolFile thunbnail = new QToolFile("thunbnail");
		QToolFile file1 = new QToolFile("img0");
		QToolFile file2 = new QToolFile("img1");
		QToolFile file3 = new QToolFile("img2");
		QToolFile file4 = new QToolFile("img3");
		QToolFile file5 = new QToolFile("img4");
		
		

		// 로그인 했을때 안했을때 구분
		Expression<Boolean> isFavoriteTool = Expressions.asBoolean(false).as("favorite");				
					
		if (username != null && !username.isBlank()) {
			isFavoriteTool = new CaseBuilder().when(favorite.toolIdx.isNotNull()).then(true).otherwise(false)
					.as("favorite");
		}

		
		JPQLQuery<ToolDetailviewDto> query = jpaQueryFactory.select(Projections.bean(ToolDetailviewDto.class,
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
				
				tool.settleBank,
				tool.settleAccount,
				tool.settleHost,
				
				ownerUser.nickname,
				ownerProfile.fileRename.as("ownerProfile"),
				
				category.name.as("categoryName"),
				thunbnail.fileRename.as("thunbnail"),
				file1.fileRename.as("img0"),
				file2.fileRename.as("img1"),
				file3.fileRename.as("img2"),
				file4.fileRename.as("img3"),
				file5.fileRename.as("img4"),
				isFavoriteTool
				
				
				
				)).from(tool)
				.leftJoin(thunbnail).on(thunbnail.toolFileIdx.eq(tool.thunbnail))
				.leftJoin(file1).on(file1.toolFileIdx.eq(tool.img1))
				.leftJoin(file2).on(file2.toolFileIdx.eq(tool.img2))
				.leftJoin(file3).on(file3.toolFileIdx.eq(tool.img3))
				.leftJoin(file4).on(file4.toolFileIdx.eq(tool.img4))
				.leftJoin(file5).on(file5.toolFileIdx.eq(tool.img5))
				.leftJoin(ownerUser).on(ownerUser.username.eq(tool.owner))
//				.leftJoin(review).on(review.toolIdx.eq(tool.toolIdx))
				.leftJoin(category).on(category.categoryIdx.eq(tool.category))
				.leftJoin(ownerProfile).on(ownerProfile.profileFileIdx.eq(ownerUser.profileImg));
		
		if(username != null && !username.isBlank()) {
			query.leftJoin(favorite)
					.on(favorite.toolIdx.eq(tool.toolIdx).and(favorite.userUsername.eq(username)));
		}
				
		
		ToolDetailviewDto toolDetail = query
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
				toolFile.fileRename.as("thunbnail"),
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
	
	// 공구 리뷰
		public Map<String,Object> toolReview (Integer toolIdx, PageRequest pageRequest,Integer orderNo) {

			QUser user = QUser.user;	
			QReviewTool reviewTool = QReviewTool.reviewTool;
			
			QReviewFile img1File = new QReviewFile("img1File");
			QReviewFile img2File = new QReviewFile("img2File");
			QReviewFile img3File = new QReviewFile("img3File");
			
			QProfileFile profile = QProfileFile.profileFile;
			
			//평점 높은순
			OrderSpecifier<?> order;

			if(orderNo == 1) {
			    order = reviewTool.score.desc();  // 평점 높은 순
			} else if(orderNo == 2) {
			    order = reviewTool.score.asc();   // 평점 낮은 순
			} else {
			    order = reviewTool.createdate.desc(); // 최신순
			}
			

			List<ToolReviewDto> reviews = jpaQueryFactory
					.select(Projections.bean(ToolReviewDto.class, 
							reviewTool.reviewToolIdx, 
							reviewTool.score,
							reviewTool.content, 
							reviewTool.createdate, 
							img1File.fileRename.as("img1"), 
							img2File.fileRename.as("img2"),
							img3File.fileRename.as("img3"),
							user.nickname.as("writer"),
							profile.fileRename.as("writerImg")
							))
					
					.from(reviewTool).leftJoin(user)
					.on(reviewTool.writer.eq(user.username))
					.leftJoin(profile).on(user.profileImg.eq(profile.profileFileIdx))
					.leftJoin(img1File).on(reviewTool.img1.eq(img1File.reviewFileIdx))
					.leftJoin(img2File).on(reviewTool.img2.eq(img2File.reviewFileIdx))
					.leftJoin(img3File).on(reviewTool.img3.eq(img3File.reviewFileIdx))
					
					.where(reviewTool.toolIdx.eq(toolIdx))
					.orderBy(order)
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize()).fetch();
			
			long totalCount = jpaQueryFactory
		            .select(reviewTool.count())
		            .from(reviewTool)
		            .where(reviewTool.toolIdx.eq(toolIdx))
		            .fetchOne();
			
			Map<String,Object> reviewPage = new HashMap<>();
			reviewPage.put("reviews", reviews);
			reviewPage.put("totalCount", totalCount);
			
			return reviewPage;
		}
	


}
