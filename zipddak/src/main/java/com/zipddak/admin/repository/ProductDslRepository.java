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
import com.zipddak.entity.QProductFile;
import com.zipddak.entity.QReviewProduct;
import com.zipddak.entity.QSeller;

@Repository
public class ProductDslRepository {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	// ProductCardDto 타입으로 반환
	public List<ProductDto> productList(Integer sortId, Integer cate1, Integer cate2) throws Exception {
		
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
		
		if(cate1 == 1 || cate1 == 2) {

			return jpaQueryFactory.select(Projections.bean(ProductDto.class, 
					product.productIdx,
					product.name,
					product.discount,
					product.salePrice,
					product.sellerUsername,
					productFile.fileRename,
					productFile.storagePath,
					review.score.avg().coalesce(0.0).as("avgRating"),
                    review.count().as("reviewCount")
            ))
            .from(product)
            .leftJoin(review).on(review.productIdx.eq(product.productIdx))
            .leftJoin(productFile).on(productFile.productFileIdx.eq(product.thumbnailFileIdx))
            .leftJoin(seller).on(seller.username.eq(product.sellerUsername))
            .groupBy(product.productIdx)
            .fetch();
				
				
		}else { // 키워드가 없을 경우
			// 정렬조건, 카테고리에 맞는 상품들을 보여줌
			return null;
		}
		
		
	}

}
