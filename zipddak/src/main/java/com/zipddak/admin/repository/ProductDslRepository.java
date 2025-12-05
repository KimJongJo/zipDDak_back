package com.zipddak.admin.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.admin.dto.ProductCardDto;
import com.zipddak.entity.QCategory;
import com.zipddak.entity.QFavoritesProduct;
import com.zipddak.entity.QOrderItem;
import com.zipddak.entity.QProduct;
import com.zipddak.entity.QProductFile;
import com.zipddak.entity.QReviewProduct;
import com.zipddak.entity.QSeller;

@Repository
public class ProductDslRepository {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	// ProductCardDto 타입으로 반환
	public List<ProductCardDto> productList(String keyword, PageRequest pageRequest, Integer sortId, Integer cate1, Integer cate2) throws Exception {
		
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
		
		// 썸네일 이미지 파일 조인
		
		
		BooleanBuilder where = new BooleanBuilder();

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
		
		if(keyword != null || keyword.isBlank()) {
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

		
		
		return jpaQueryFactory
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
		                seller.brandName
		                
		        ))
		        .from(product)
		        .leftJoin(review).on(review.productIdx.eq(product.productIdx))
		        .leftJoin(productFile).on(productFile.productFileIdx.eq(product.thumbnailFileIdx))
		        .leftJoin(seller).on(seller.username.eq(product.sellerUsername))
		        .leftJoin(category).on(category.categoryIdx.eq(product.subCategoryIdx))
		        .leftJoin(orderItem).on(orderItem.product.productIdx.eq(product.productIdx))
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

}
