package com.zipddak.admin.repository;

import java.util.List;

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
import com.zipddak.admin.dto.OptionListDto;
import com.zipddak.admin.dto.OrderListDto;
import com.zipddak.admin.dto.OrderListResponseDto;
import com.zipddak.admin.dto.ProductCardDto;
import com.zipddak.admin.dto.ProductDetailDto;
import com.zipddak.admin.dto.ProductImagesDto;
import com.zipddak.admin.dto.ProductInquiriesDto;
import com.zipddak.admin.dto.ProductReviewsDto;
import com.zipddak.entity.Inquiries;
import com.zipddak.entity.QCategory;
import com.zipddak.entity.QFavoritesProduct;
import com.zipddak.entity.QInquiries;
import com.zipddak.entity.QOrderItem;
import com.zipddak.entity.QProduct;
import com.zipddak.entity.QProductFile;
import com.zipddak.entity.QProductOption;
import com.zipddak.entity.QReviewFile;
import com.zipddak.entity.QReviewProduct;
import com.zipddak.entity.QSeller;
import com.zipddak.entity.QUser;

@Repository
public class ProductDslRepository {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	// ProductCardDto 타입으로 반환
	// 자재 상품 목록 조회
	public List<ProductCardDto> productList(String keyword, PageRequest pageRequest, Integer sortId, Integer cate1, Integer cate2, String username) throws Exception {
		
		// 자재 상품
		QProduct product = QProduct.product;
		// 리뷰 평점, 리뷰 수
		QReviewProduct review = QReviewProduct.reviewProduct;
		// 로그인 한 사용자가 관심 상품으로 등록했는지 여부
		QFavoritesProduct favorite = QFavoritesProduct.favoritesProduct;
		// 인기순 정렬에서 사용
		QOrderItem orderItem = QOrderItem.orderItem;
		// 썸네일 이미지
		QProductFile productFile = QProductFile.productFile;
		// 상품 판매 업체
		QSeller seller = QSeller.seller;
		// 상품 카테고리
		QCategory category = QCategory.category;
		
		// 정렬조건
		// 1 -> 인기순
		// 2 -> 최신순
		// 3 -> 낮은 가격순
		// 4 -> 높은 가격순
		
		// 카테고리
		// 카테고리가 1이나 2일경우
		// cate2까지 보여주기
		
		// 카테고리가 3 이상일 경우
		// cate2는 없음
		
		//로그인 했을때 안했을때 구분
		Expression<Boolean> isFavoriteExpr = Expressions.asBoolean(false);

		if (username != null && !username.isBlank()) {
		    isFavoriteExpr = new CaseBuilder()
		            .when(favorite.productIdx.isNotNull())
		            .then(true)
		            .otherwise(false)
		            .as("favorite");
		}

		
		BooleanBuilder where = new BooleanBuilder();
 
		// 상품 공개 유무가 1인 상품만
		where.and(product.visibleYn.eq(true));
		
		if (cate1 == 1 || cate1 == 2) {
			if(cate2 == 1) {
				where.and(product.categoryIdx.eq(cate1));
			}else {
				where.and(product.categoryIdx.eq(cate1))
		         .and(product.subCategoryIdx.eq(cate2));
			}
		} else {
		    where.and(product.categoryIdx.eq(cate1));
		}
		
		if(keyword != null && !keyword.isBlank()) {
			where.and(product.name.contains(keyword));
		}
		
		OrderSpecifier<?> order;

		switch (sortId) {
		    case 1: // 인기순 (판매량 많은 순)
		        order = orderItem.quantity.sum().coalesce(0).desc();
		        break;

		    case 2: // 최신순 (상품 등록일)
		        order = product.createdAt.desc();
		        break;

		    case 3: // 가격 낮은순
		        order = product.salePrice.asc();
		        break;
		        
		    case 4: // 가격 낮은순
		    	order = product.salePrice.desc();
		        break;

		    default: // 평점 높은순
		    	order = review.score.avg().coalesce(0.0).desc();
		}

		
		JPQLQuery<ProductCardDto> query = jpaQueryFactory
		        .select(Projections.bean(ProductCardDto.class,
		                product.productIdx,
		                product.name,
		                product.discount,
		                product.salePrice,
		                product.sellerUsername,
		                productFile.fileRename,
		                productFile.storagePath,
		                Expressions.numberTemplate(
		                        Double.class,
		                        "ROUND({0}, 1)",
		                        review.score.avg().coalesce(0.0)
		                ).as("avgRating"),
		                // count는 항상 Long 타입으로 반환
		                review.count().as("reviewCount"),
		                seller.brandName,
		                isFavoriteExpr
		                
		        ))
		        .from(product)
		        .leftJoin(review).on(review.productIdx.eq(product.productIdx))
		        .leftJoin(productFile).on(productFile.productFileIdx.eq(product.thumbnailFileIdx))
		        .leftJoin(seller).on(seller.username.eq(product.sellerUsername))
		        .leftJoin(category).on(category.categoryIdx.eq(product.subCategoryIdx))
		        .leftJoin(orderItem).on(orderItem.product.productIdx.eq(product.productIdx));
		
		if(username != null && !username.isBlank()) {
			query.leftJoin(favorite)
				.on(favorite.productIdx.eq(product.productIdx)
						.and(favorite.userUsername.eq(username)));
		}
		
		return query
				.where(where)
		        .groupBy(
		        	    product.productIdx,
		        	    product.name,
		        	    product.discount,
		        	    product.salePrice,
		        	    product.sellerUsername,
		        	    productFile.fileRename,
		        	    productFile.storagePath
		        	)
		        // 판매 이력이 없는 상품은 null이 될 수 있음 -> null일경우 판매 횟수 0으로 처리
		        .orderBy(order)
		        .offset(pageRequest.getOffset())
		        .limit(pageRequest.getPageSize())
		        .fetch();

	}
	
	// 상품 상세 정보
	public ProductDetailDto productInfo(Integer productId) {
		
		QProduct product = QProduct.product;
		QCategory category1 = new QCategory("category1");
		QCategory category2 = new QCategory("category2");

		QSeller seller = QSeller.seller;
		
		return jpaQueryFactory.select(Projections.bean(ProductDetailDto.class, 
					category1.name.as("category"),
					category2.name.as("subCategory"),
					product.productIdx,
					product.name,
					product.discount,
					product.price,
					product.salePrice,
					product.postCharge,
					product.optionYn,
					product.postType,
					product.postYn,
					product.pickupYn,
					product.zonecode,
					product.pickupAddr1,
					product.pickupAddr2,
					seller.brandName
				))
				.from(product)
				.leftJoin(category1).on(category1.categoryIdx.eq(product.categoryIdx))
				.leftJoin(category2).on(category2.categoryIdx.eq(product.subCategoryIdx))
				.leftJoin(seller).on(seller.username.eq(product.sellerUsername))
				.where(product.productIdx.eq(productId))
				.fetchFirst();
	}


	// 해당 상품에 해당하는 문의 불러오기
	public List<ProductInquiriesDto> productInquiries(Integer productId, PageRequest pageRequest) {

		// 문의
		QInquiries inquiries = QInquiries.inquiries;
		// 글쓴이
		QUser user = QUser.user;
		// 상품
		QProduct product = QProduct.product;
		// 판매업체
		QSeller seller = QSeller.seller;
		
		return jpaQueryFactory.select(Projections.bean(ProductInquiriesDto.class,
				inquiries.inquiryIdx,
				user.nickname.as("writerNickname"),
				inquiries.content,
				inquiries.answer,
				inquiries.writeAt,
				inquiries.answerAt,
				seller.brandName
				))
				.from(inquiries)
				.leftJoin(user).on(inquiries.writerUsername.eq(user.username))
				.leftJoin(product).on(inquiries.productIdx.eq(product.productIdx))
				.leftJoin(seller).on(product.sellerUsername.eq(seller.username))
				.where(product.productIdx.eq(productId).and(inquiries.answer.isNotNull()))
				.orderBy(inquiries.answerAt.desc())
				.offset(pageRequest.getOffset())
				.limit(pageRequest.getPageSize())
				.fetch();
	}

	// 해당 상품에 해당하는 리뷰 불러오기
	public List<ProductReviewsDto> productReviews(Integer productId, PageRequest pageRequest) {

		QReviewProduct reviewProduct = QReviewProduct.reviewProduct;
		QUser user = QUser.user;
		QReviewFile reviewFile = QReviewFile.reviewFile;
		
		return jpaQueryFactory.select(Projections.bean(ProductReviewsDto.class, 
					reviewProduct.reviewProductIdx,
					reviewProduct.score,
					reviewProduct.content,
					reviewProduct.createdate,
					user.nickname,
					reviewFile.fileRename.as("img1Name"),
					reviewFile.fileRename.as("img2Name"),
					reviewFile.fileRename.as("img3Name"),
					reviewFile.storagePath.as("img1Path"),
					reviewFile.storagePath.as("img2Path"),
					reviewFile.storagePath.as("img3Path")
				))
				.from(reviewProduct)
				.leftJoin(user).on(reviewProduct.writer.eq(user.username))
				.leftJoin(reviewFile).on(reviewProduct.img1.eq(reviewFile.reviewFileIdx))
				.leftJoin(reviewFile).on(reviewProduct.img2.eq(reviewFile.reviewFileIdx))
				.leftJoin(reviewFile).on(reviewProduct.img3.eq(reviewFile.reviewFileIdx))
				.where(reviewProduct.productIdx.eq(productId))
				.orderBy(reviewProduct.createdate.desc())
				.offset(pageRequest.getOffset())
				.limit(pageRequest.getPageSize())
				.fetch();
	}

	// 구매 목록의 자재 정보
	public OrderListResponseDto orderListResponse(Integer productId) {

		QProduct product = QProduct.product;
		QSeller seller = QSeller.seller;
		
		return jpaQueryFactory.select(Projections.bean(OrderListResponseDto.class, 
					product.name.as("productName"),
					product.postCharge,
					product.salePrice,
					seller.brandName
				))
				.from(product)
				.leftJoin(seller).on(product.sellerUsername.eq(seller.username))
				.where(product.productIdx.eq(productId))
				.fetchFirst();
	}

	// 옵션에 대한 정보를 반환해야함
	public OptionListDto requestOptions(Integer optionId) {
		
		QProductOption productOption = QProductOption.productOption;
		
		return jpaQueryFactory.select(Projections.bean(OptionListDto.class,
					productOption.productOptionIdx.as("optionId"),
					productOption.name,
					productOption.value,
					productOption.price
				))
				.from(productOption)
				.where(productOption.productOptionIdx.eq(optionId))
				.fetchOne();
	}



	

}
