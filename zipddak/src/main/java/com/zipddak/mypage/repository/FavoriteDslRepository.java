package com.zipddak.mypage.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.entity.QFavoritesProduct;
import com.zipddak.entity.QProduct;
import com.zipddak.entity.QProductFile;
import com.zipddak.entity.QReviewProduct;
import com.zipddak.entity.QSeller;
import com.zipddak.mypage.dto.FavoriteProductDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FavoriteDslRepository {

	private final JPAQueryFactory jpaQueryFactory;

	// 관심 상품목록 조회
	public List<FavoriteProductDto> selectFavoriteProductList(String username, PageRequest pageRequest)
			throws Exception {

		QFavoritesProduct favoritesProduct = QFavoritesProduct.favoritesProduct;
		QProduct product = QProduct.product;
		QSeller seller = QSeller.seller;
		QReviewProduct reviewProduct = QReviewProduct.reviewProduct;
		QProductFile productFile = QProductFile.productFile;

		return jpaQueryFactory
				.select(Projections.constructor(FavoriteProductDto.class, product.productIdx, product.name,
						productFile.storagePath, product.salePrice, product.discount, seller.brandName,
						reviewProduct.score.avg().coalesce(0.0).intValue(), reviewProduct.count()))
				.from(favoritesProduct).leftJoin(product).on(product.productIdx.eq(favoritesProduct.productIdx))
				.leftJoin(seller).on(product.sellerUsername.eq(seller.user.username)).leftJoin(reviewProduct)
				.on(product.productIdx.eq(reviewProduct.productIdx)).leftJoin(productFile)
				.on(product.thumbnailFileIdx.eq(productFile.productFileIdx))
				.where(favoritesProduct.userUsername.eq(username)).offset(pageRequest.getOffset())
				.groupBy(product.productIdx, product.name, productFile.storagePath, product.salePrice, product.discount,
						seller.brandName)
				.limit(pageRequest.getPageSize()).fetch();
	}

	// 관심 상품목록 개수
	public Long selectFavoriteProductCount(String username) throws Exception {
		QFavoritesProduct favoritesProduct = QFavoritesProduct.favoritesProduct;

		return jpaQueryFactory.select(favoritesProduct.count()).from(favoritesProduct)
				.where(favoritesProduct.userUsername.eq(username)).fetchOne();
	}

}
