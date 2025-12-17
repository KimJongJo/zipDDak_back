package com.zipddak.admin.repository;

import java.sql.Date;
import java.text.SimpleDateFormat;
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
import com.zipddak.admin.dto.AdminSaleListDto;
import com.zipddak.admin.dto.AdminSellerListDto;
import com.zipddak.admin.dto.AdminUserListDto;
import com.zipddak.admin.dto.ResponseAdminListDto;
import com.zipddak.dto.AdminPaymentListDto;
import com.zipddak.entity.Order.PaymentStatus;
import com.zipddak.entity.Payment.PaymentType;
import com.zipddak.entity.QCategory;
import com.zipddak.entity.QExpert;
import com.zipddak.entity.QOrder;
import com.zipddak.entity.QPayment;
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

	
	
	public ResponseAdminListDto rentalList(Integer column, Integer state, String keyword, Integer page, String startDate,
			String endDate) {
		
		
		int itemsPerPage = 15; 
		int buttonsPerPage = 5;
		
		QRental rental = QRental.rental;
		QTool tool = QTool.tool;
		QUser borrower = new QUser("borrower");
		QUser owner = new QUser("owner");
		
		BooleanBuilder where = new BooleanBuilder();
		
		RentalStatus stringState = null;
		switch(state) {
		case 1 : stringState = RentalStatus.PRE; break;
		case 2 : stringState = RentalStatus.PAYED; break;
		case 3 : stringState = RentalStatus.DELIVERY; break;
		case 4 : stringState = RentalStatus.RENTAL; break;
		case 5 : stringState = RentalStatus.RETURN; break;
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
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (startDate != null && !startDate.isBlank()) {
		    try {
		        Date start = Date.valueOf(startDate); // 문자열 → java.sql.Date
		        where.and(rental.startDate.goe(start)); // startDate 이후
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}

		if (endDate != null && !endDate.isBlank()) {
		    try {
		        Date end = Date.valueOf(endDate); // 문자열 → java.sql.Date
		        where.and(rental.endDate.loe(end)); // endDate 이전
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		
		// 3️⃣ count 쿼리
		JPAQuery<Long> rentalQuery = jpaQueryFactory
			    .select(rental.count())
			    .from(rental)
			    .leftJoin(tool).on(rental.tool.toolIdx.eq(tool.toolIdx))
			    .leftJoin(owner).on(rental.owner.eq(owner.username))
			    .leftJoin(borrower).on(rental.borrower.eq(borrower.username))
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
	    
	    List<AdminRentalListDto> rentalList = jpaQueryFactory.select(Projections.bean(AdminRentalListDto.class, 
	    			rental.rentalIdx,
	    			tool.name.as("toolName"),
	    			owner.name.as("owner"),
	    			borrower.name.as("borrower"),
	    			rental.startDate,
	    			rental.endDate,
	    			rental.satus.as("state")
	    		)) 
	    		.from(rental)
	    		.leftJoin(tool).on(rental.tool.toolIdx.eq(tool.toolIdx))
	    		.leftJoin(owner).on(rental.owner.eq(owner.username))
	    		.leftJoin(borrower).on(rental.borrower.eq(borrower.username))
	    		.where(where)
	    		.offset((page - 1) * itemsPerPage)
	    		.limit(itemsPerPage)
	    		.fetch();
		
		return new ResponseAdminListDto(rentalList, pageInfo);
	}

	
	public ResponseAdminListDto saleList(Integer column, Integer state, String keyword, Integer page, String startDate,
			String endDate) {
	
		int itemsPerPage = 15; 
		int buttonsPerPage = 5;
		
		QOrder order = QOrder.order;
		QUser buyer = new QUser("buyer");
		
		BooleanBuilder where = new BooleanBuilder();
		
		PaymentStatus stringState = null;
		switch(state) {
		case 1 : stringState = PaymentStatus.결제완료; break;
		case 2 : stringState = PaymentStatus.결제취소; break;
		}
		
		if(stringState != null) {
			where.and(order.paymentStatus.eq(stringState));
		}
		
		
		// 2️⃣ 검색 조건
		if (keyword != null && !keyword.isBlank()) {
		    switch (column) {
		        case 2: // 주문ID
		            where.and(order.orderIdx.stringValue().contains(keyword));
		            break;
		        case 3: // 주문코드
		            where.and(order.orderCode.contains(keyword));
		            break;
		        case 4: // 구매자
		            where.and(buyer.name.contains(keyword));
		            break;
		        default:
		            // all 검색
		            where.and(
		            		order.orderIdx.stringValue().contains(keyword)
		                    .or(order.orderCode.contains(keyword))
		                    .or(buyer.name.contains(keyword))
		            );
		            break;
		    }
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (startDate != null && !startDate.isBlank()) {
		    try {
		        Date start = Date.valueOf(startDate); // 문자열 → java.sql.Date
		        where.and(order.createdAt.goe(start)); // startDate 이후
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}

		if (endDate != null && !endDate.isBlank()) {
		    try {
		        Date end = Date.valueOf(endDate); // 문자열 → java.sql.Date
		        where.and(order.createdAt.loe(end)); // endDate 이전
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		
		// 3️⃣ count 쿼리
		JPAQuery<Long> orderQuery = jpaQueryFactory
			    .select(order.count())
			    .from(order)
			    .leftJoin(buyer).on(order.user.username.eq(buyer.username))
			    .where(where);
				
		Long totalCount = orderQuery.fetchOne();
		
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
	    
	    List<AdminSaleListDto> saleList = jpaQueryFactory.select(Projections.bean(AdminSaleListDto.class, 
	    			order.orderIdx,
	    			order.orderCode,
	    			buyer.name.as("buyer"),
	    			order.postRecipient.as("recv"),
	    			order.totalAmount.as("amount"),
	    			order.createdAt,
	    			order.paymentStatus.as("state")
	    		))
	    		.from(order)
	    		.leftJoin(buyer).on(order.user.username.eq(buyer.username))
	    		.where(where)
	    		.offset((page - 1) * itemsPerPage)
	    		.limit(itemsPerPage)
	    		.fetch();
	    
		return new ResponseAdminListDto(saleList, pageInfo);
	}

	public ResponseAdminListDto paymentList(Integer type, Integer state, String keyword, Integer page) {
	
		int itemsPerPage = 15; 
		int buttonsPerPage = 5;
		
		QPayment payment = QPayment.payment;
		
		BooleanBuilder where = new BooleanBuilder();
		
		PaymentType paymentType = null;
		
		switch(type) {
		case 2 : paymentType = PaymentType.RENTAL; break;
		case 3 : paymentType = PaymentType.MATCHING; break;
		case 4 : paymentType = PaymentType.ORDER; break;
		case 5 : paymentType = PaymentType.MEMBERSHIP; break;
		}
		
		if(paymentType != null) {
			where.and(payment.paymentType.eq(paymentType));
		}
		
		String stringState = null;
		switch(state) {
		case 1 : stringState = "DONE"; break;
		case 2 : stringState = "CANCELED"; break;
		}
		
		 where.and(payment.status.eq(stringState));
		
		// 2️⃣ 검색 조건
		if (keyword != null && !keyword.isBlank()) {
			where.and(
                payment.paymentIdx.stringValue().contains(keyword)
                    .or(payment.orderId.contains(keyword))
                    .or(payment.orderName.contains(keyword))
            );
		}
		
		// 3️⃣ count 쿼리
		JPAQuery<Long> paymentQuery = jpaQueryFactory
		    .select(payment.count())
		    .from(payment)
		    .where(where);
				
		Long totalCount = paymentQuery.fetchOne();
		
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
		
	    List<AdminPaymentListDto> paymentList = jpaQueryFactory.select(Projections.bean(AdminPaymentListDto.class, 
	    			payment.paymentIdx,
	    			payment.orderId,
	    			payment.orderName,
	    			payment.approvedAt,
	    			payment.method,
	    			payment.totalAmount.as("amount"),
	    			payment.status.as("state")
	    		))
	    		.from(payment)
	    		.where(where)
	    		.offset((page - 1) * itemsPerPage)
	    		.limit(itemsPerPage)
	    		.fetch();
	    
		return new ResponseAdminListDto(paymentList, pageInfo);
	}
	
}

