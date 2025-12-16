package com.zipddak.admin.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.admin.dto.AdminExpertListDto;
import com.zipddak.admin.dto.AdminRentalListDto;
import com.zipddak.admin.dto.AdminSellerListDto;
import com.zipddak.admin.dto.AdminUserListDto;
import com.zipddak.admin.dto.ResponseAdminListDto;
import com.zipddak.entity.QCategory;
import com.zipddak.entity.QExpert;
import com.zipddak.entity.QRental;
import com.zipddak.entity.QReportExpert;
import com.zipddak.entity.QReportSeller;
import com.zipddak.entity.QSeller;
import com.zipddak.entity.QTool;
import com.zipddak.entity.QUser;
import com.zipddak.entity.Rental.RentalStatus;
import com.zipddak.entity.User.UserRole;
import com.zipddak.entity.User.UserState;
import com.zipddak.util.PageInfo;

@Repository
public class AdminDslRepository {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	public ResponseAdminListDto userList(Integer state, Integer column, String keyword, Integer page) {

		int itemsPerPage = 15; 
		int buttonsPerPage = 5;
		
		QUser user = QUser.user;

		BooleanBuilder where = new BooleanBuilder();

		
		
		where.and(user.role.eq(UserRole.USER));
		// 1️⃣ 상태 조건
		if (state != 1) { // 1 = 전체
		    UserState userState;

		    switch (state) {
		        case 2:
		            userState = UserState.ACTIVE;
		            break;
		        case 3:
		            userState = UserState.SUSPENDED;
		            break;
		        default:
		            userState = UserState.WITHDRAWN;
		            break;
		    }

		    where.and(user.state.eq(userState));
		}

		// 2️⃣ 검색 조건
		if (keyword != null && !keyword.isBlank()) {
		    switch (column) {
		        case 2: // name
		            where.and(user.name.contains(keyword));
		            break;
		        case 3: // nickname
		            where.and(user.nickname.contains(keyword));
		            break;
		        case 4: // username
		            where.and(user.username.contains(keyword));
		            break;
		        case 5: // tel
		            where.and(user.phone.contains(keyword));
		            break;
		        default:
		            // all 검색
		            where.and(
		                user.name.contains(keyword)
		                    .or(user.nickname.contains(keyword))
		                    .or(user.username.contains(keyword))
		                    .or(user.phone.contains(keyword))
		            );
		            break;
		    }
		}

		// 3️⃣ count 쿼리
		JPAQuery<Long> userQuery = jpaQueryFactory
		    .select(user.count())
		    .from(user)
		    .where(where);

		Long totalCount = userQuery.fetchOne();
		
		 // 2. 페이징 계산
	    int allPage = (int) Math.ceil((double) totalCount / itemsPerPage);

	    int startPage = ((page - 1) / buttonsPerPage) * buttonsPerPage + 1;
	    int endPage = Math.min(startPage + buttonsPerPage - 1, allPage);

	    // 4. PageInfo 세팅
	    PageInfo pageInfo = new PageInfo();
	    pageInfo.setCurPage(page);
	    pageInfo.setAllPage(allPage);
	    pageInfo.setStartPage(startPage);
	    pageInfo.setEndPage(endPage);
	    
	    List<AdminUserListDto> userList = jpaQueryFactory.select(Projections.bean(AdminUserListDto.class, 
	    			user.name,
	    			user.nickname,
	    			user.username,
	    			user.phone,
	    			user.createdate.as("createdAt"),
	    			user.state
	    		))
	    		.from(user)
	    		.where(where)
	    		.orderBy(user.createdate.desc())
	    		.offset((page - 1) * itemsPerPage)
	    		.limit(itemsPerPage)
	    		.fetch();
		
		return new ResponseAdminListDto(userList, pageInfo);
	}

	public ResponseAdminListDto expertList(Integer major, Integer state, Integer column, String keyword, Integer page) {
		
		int itemsPerPage = 15; 
		int buttonsPerPage = 5;
		
		QUser user = QUser.user;
		QExpert expert = QExpert.expert;
		QCategory category = QCategory.category;
		QReportExpert report = QReportExpert.reportExpert;

		BooleanBuilder where = new BooleanBuilder();
		
		if(major != 0) {
			where.and(expert.mainServiceIdx.eq(major));
		}
		
		
		String stringState = null;
		switch(state) {
		case 2 : stringState = "ACTIVE"; break;
		case 3 : stringState = "STOPPED"; break;
		case 4 : stringState = "WAITING"; break;
		}
		
		if(stringState != null) where.and(expert.activityStatus.eq(stringState));
		
		// 2️⃣ 검색 조건
		if (keyword != null && !keyword.isBlank()) {
		    switch (column) {
		        case 2: // name
		            where.and(expert.activityName.contains(keyword));
		            break;
		        case 3: // nickname
		            where.and(user.username.contains(keyword));
		            break;
		        case 4: // username
		            where.and(user.phone.contains(keyword));
		            break;
		        default:
		            // all 검색
		            where.and(
		                user.name.contains(keyword)
		                    .or(expert.activityName.contains(keyword))
		                    .or(user.username.contains(keyword))
		                    .or(user.phone.contains(keyword))
		            );
		            break;
		    }
		}
		
		// 3️⃣ count 쿼리
		JPAQuery<Long> expertQuery = jpaQueryFactory
		    .select(expert.count())
		    .from(expert)
		    .leftJoin(user).on(user.username.eq(expert.user.username))
		    .where(where);
				
		Long totalCount = expertQuery.fetchOne();
		
		 // 2. 페이징 계산
	    int allPage = (int) Math.ceil((double) totalCount / itemsPerPage);

	    int startPage = ((page - 1) / buttonsPerPage) * buttonsPerPage + 1;
	    int endPage = Math.min(startPage + buttonsPerPage - 1, allPage);

	    // 4. PageInfo 세팅
	    PageInfo pageInfo = new PageInfo();
	    pageInfo.setCurPage(page);
	    pageInfo.setAllPage(allPage);
	    pageInfo.setStartPage(startPage);
	    pageInfo.setEndPage(endPage);
		
	    List<AdminExpertListDto> expertList = jpaQueryFactory.select(Projections.bean(AdminExpertListDto.class,
	    			expert.expertIdx,
	    			user.name,
	    			expert.activityName,
	    			user.username,
	    			report.reportIdx.countDistinct().as("reportCount"),
	    			category.name.as("cateName"),
	    			user.phone,
	    			expert.createdAt,
	    			expert.activityStatus.as("state")
	    		))
	    		.from(expert)
	    		.leftJoin(user).on(expert.user.username.eq(user.username))
	    		.leftJoin(category).on(expert.mainServiceIdx.eq(category.categoryIdx))
	    		.leftJoin(report).on(report.expertIdx.eq(expert.expertIdx))
	    		.where(where)
	    		.groupBy(expert.expertIdx, user.name, expert.activityName, user.username, category.name, user.phone, expert.createdAt, expert.activityStatus)
	    		.orderBy(expert.createdAt.desc())
	    		.offset((page - 1) * itemsPerPage)
	    		.limit(itemsPerPage)
	    		.fetch();
		
		return new ResponseAdminListDto(expertList, pageInfo);
	}

	
	
	
	public ResponseAdminListDto sellerList(Integer productCode, Integer state, String keyword, Integer page) {
		
		int itemsPerPage = 15; 
		int buttonsPerPage = 5;
		
		QSeller seller = QSeller.seller;
		QReportSeller report = QReportSeller.reportSeller;
		
		BooleanBuilder where = new BooleanBuilder();
		
		if(productCode != 0) {
			// ,를 기준으로 찾음
			BooleanExpression categoryContains = Expressions.booleanTemplate(
				    "FIND_IN_SET({0}, {1}) > 0",
				    productCode,
				    seller.handleItemCateIdx
				);
			
			where.and(categoryContains);
		}
		
		
		String stringState = null;
		switch(state) {
		case 2 : stringState = "ACTIVE"; break;
		case 3 : stringState = "STOPPED"; break;
		case 4 : stringState = "WAITING"; break;
		}
		
		if(stringState != null) where.and(seller.activityStatus.eq(stringState));
		
		// 2️⃣ 검색 조건
		if (keyword != null && !keyword.isBlank()) {
			where.and(
                seller.brandName.contains(keyword)
                    .or(seller.user.username.contains(keyword))
                    .or(seller.ceoName.contains(keyword))
                    .or(seller.managerTel.contains(keyword))
            );
		}
		
		// 3️⃣ count 쿼리
		JPAQuery<Long> sellerQuery = jpaQueryFactory
		    .select(seller.count())
		    .from(seller)
		    .where(where);
				
		Long totalCount = sellerQuery.fetchOne();
		
		 // 2. 페이징 계산
	    int allPage = (int) Math.ceil((double) totalCount / itemsPerPage);

	    int startPage = ((page - 1) / buttonsPerPage) * buttonsPerPage + 1;
	    int endPage = Math.min(startPage + buttonsPerPage - 1, allPage);

	    // 4. PageInfo 세팅
	    PageInfo pageInfo = new PageInfo();
	    pageInfo.setCurPage(page);
	    pageInfo.setAllPage(allPage);
	    pageInfo.setStartPage(startPage);
	    pageInfo.setEndPage(endPage);
	    
	    List<AdminSellerListDto> sellerList = jpaQueryFactory.select(Projections.bean(AdminSellerListDto.class, 
	    			seller.sellerIdx,
	    			seller.compName,
	    			seller.brandName,
	    			seller.user.username,
	    			seller.ceoName,
	    			report.reportIdx.countDistinct().as("reportCount"),
	    			seller.managerTel,
	    			seller.createdAt,
	    			seller.activityStatus.as("state")
	    		))
	    		.from(seller)
	    		.leftJoin(report).on(report.sellerUsername.eq(seller.user.username))
	    		.where(where)
	    		.groupBy(seller.sellerIdx)
	    		.offset((page - 1) * itemsPerPage)
	    		.limit(itemsPerPage)
	    		.fetch();
	    
	    
		return new ResponseAdminListDto(sellerList, pageInfo);
	}

	
	
	public ResponseAdminListDto rentalList(Integer column, Integer state, String keyword, Integer page, Date startDate,
			Date endDate) {
		
		
		int itemsPerPage = 15; 
		int buttonsPerPage = 5;
		
		QRental rental = QRental.rental;
		QTool tool = QTool.tool;
		QUser borrower = new QUser("borrower");
		QUser owner = new QUser("owner");
		
		BooleanBuilder where = new BooleanBuilder();
		
		RentalStatus stringState = null;
		switch(state) {
		case 2 : stringState = RentalStatus.PRE; break;
		case 3 : stringState = RentalStatus.PAYED; break;
		case 4 : stringState = RentalStatus.DELIVERY; break;
		case 5 : stringState = RentalStatus.RENTAL; break;
		case 6 : stringState = RentalStatus.RETURN; break;
		}
		
		if(stringState != null) {
			where.and(rental.satus.eq(stringState));
		}
		
		// 2️⃣ 검색 조건
		if (keyword != null && !keyword.isBlank()) {
		    switch (column) {
		        case 2: // name
		            where.and(tool.name.contains(keyword));
		            break;
		        case 3: // nickname
		            where.and(owner.name.contains(keyword));
		            break;
		        case 4: // username
		            where.and(borrower.name.contains(keyword));
		            break;
		        default:
		            // all 검색
		            where.and(
		            		tool.name.contains(keyword)
		                    .or(owner.name.contains(keyword))
		                    .or(borrower.name.contains(keyword))
		            );
		            break;
		    }
		}
		
		// 3️⃣ count 쿼리
		JPAQuery<Long> rentalQuery = jpaQueryFactory
		    .select(rental.count())
		    .from(rental)
		    .where(where);
				
		Long totalCount = rentalQuery.fetchOne();
		
		 // 2. 페이징 계산
	    int allPage = (int) Math.ceil((double) totalCount / itemsPerPage);

	    int startPage = ((page - 1) / buttonsPerPage) * buttonsPerPage + 1;
	    int endPage = Math.min(startPage + buttonsPerPage - 1, allPage);

	    // 4. PageInfo 세팅
	    PageInfo pageInfo = new PageInfo();
	    pageInfo.setCurPage(page);
	    pageInfo.setAllPage(allPage);
	    pageInfo.setStartPage(startPage);
	    pageInfo.setEndPage(endPage);
	    
//	    List<AdminRentalListDto> rentalList = jpaQueryFactory.select(Projections.bean(AdminRentalListDto.class, 
//	    			rental.rentalIdx,
//	    			tool.name.as("toolName"),
//	    			owner.name.as("owner"),
//	    			borrower.name.as("borrower"),
//	    			rental.startDate,
//	    			rental.endDate,
//	    			rental.satus.as("state")
//	    		)) 
		
		
		return null;
	}
	
}
