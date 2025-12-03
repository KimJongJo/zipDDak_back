package com.zipddak.admin.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.dto.ProductDto;
import com.zipddak.entity.QFavoritesProduct;
import com.zipddak.entity.QOrderItem;
import com.zipddak.entity.QProduct;
import com.zipddak.entity.QReviewProduct;

@Repository
public class ProductDslRepository {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	public List<ProductDto> productList(Integer sortId, String keyword, Integer cate1, Integer cate2) throws Exception {
		
//		QProduct product = QProduct.product;
//		QReviewProduct review = QReviewProduct.reviewProduct;
//		QFavoritesProduct favorite = QFavoritesProduct.favoritesProduct;
//		QOrderItem orderItem = QOrderItem.orderItem;
		
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
	
		// 키워드가 있을 경우
		// 키워드에 대한 결과만 보여주기
//		if(keyword != null && !keyword.isBlank()) {
//			return jpaQueryFactory.select(Projections.bean(ProductDto.class, 
//					product.productIdx,
//					product.name,
//					product.discount,
//					product.salePrice,
//					review.score.count().as("reviewCount")
//					))
//					.from(product)
//					.leftJoin(review).on(product.productIdx.eq(review.productIdx))
//					.where(product.productIdx())
//				
//				    
//					
//					
//					
//				)); 
//		}else { // 키워드가 없을 경우
//			// 정렬조건, 카테고리에 맞는 상품들을 보여줌
//			
//		}
//		
		return null;
	}

}
