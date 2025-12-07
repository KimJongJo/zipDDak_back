package com.zipddak.seller.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.dto.CategoryDto;
import com.zipddak.dto.ProductDto;
import com.zipddak.entity.QCategory;
import com.zipddak.entity.QProduct;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SellerProductRepository {
	
	private final JPAQueryFactory jpaQueryFactory;
	
	//셀러가 등록한 상품의 카테고리만 조회 
	public List<CategoryDto> findSellerCategories(String sellerUsername) {

        QProduct product = QProduct.product;
        QCategory category = QCategory.category;

        return jpaQueryFactory.select(Projections.fields(CategoryDto.class,
										                        category.categoryIdx,
										                        category.name))
				                .distinct()
				                .from(product)
				                .join(category).on(product.categoryIdx.eq(category.categoryIdx))
				                .where(product.sellerUsername.eq(sellerUsername))
				                .orderBy(category.name.asc())
				                .fetch();
    }

	//특정 셀러가 등록한 상품 리스트 
	public List<ProductDto> findMyProducts(PageRequest pageRequest, String sellerUsername, List<Integer> visibleList, List<Integer> categoryList, String keyword) {
		QProduct product = QProduct.product;
		
		BooleanBuilder builder = new BooleanBuilder();
		
		// 셀러 아이디
	    builder.and(product.sellerUsername.eq(sellerUsername));
	    
	    // 판매 상태 (Y/N/품절 등)
//	    if (visibleList != null && !visibleList.isEmpty()) {
//	        builder.and(product.visibleYn.in(visibleList));
//	    }
	    

	    // 카테고리
	    if (categoryList != null && !categoryList.isEmpty()) {
	        builder.and(product.categoryIdx.in(categoryList));
	    }

	    // 키워드
	    if (keyword != null && !keyword.isEmpty()) {
	        builder.and(product.name.contains(keyword));
	    }
		
		List<ProductDto> ProductDto = null;
		ProductDto = jpaQueryFactory.select(Projections.fields(ProductDto.class,
										                product.productIdx,
										                product.sellerUsername,
										                product.name,
										                product.thumbnailFileIdx,
										                product.categoryIdx,
										                product.subCategoryIdx,
										                product.price,
										                product.salePrice,
										                product.visibleYn,
										                product.createdAt
										                ))
								        .from(product)
								        .where(builder)
								        .orderBy(product.productIdx.desc())
								        .offset(pageRequest.getOffset())
								        .limit(pageRequest.getPageSize())
								        .fetch();
		return ProductDto;
	}
	
	//특정 셀러가 등록한 상품 수
	public Long allMyPdCount(String sellerUsername, List<Integer> visibleList, List<Integer> categoryList, String keyword) {
		QProduct product = QProduct.product;
		
		BooleanBuilder builder = new BooleanBuilder();
	    builder.and(product.sellerUsername.eq(sellerUsername));
	    
//	    if (visibleList != null && !visibleList.isEmpty()) {
//	        builder.and(product.visibleYn.in(visibleList));
//	    }

	    if (categoryList != null && !categoryList.isEmpty()) {
	        builder.and(product.categoryIdx.in(categoryList));
	    }

	    if (keyword != null && !keyword.isEmpty()) {
	        builder.and(product.name.contains(keyword));
	    }
		
		Long myProductCnt = null;
		myProductCnt = jpaQueryFactory.select(product.count())
							            .from(product)
							            .where(builder)
							            .fetchOne();
		
		return myProductCnt;
	}
	
	
	
	
	
}
