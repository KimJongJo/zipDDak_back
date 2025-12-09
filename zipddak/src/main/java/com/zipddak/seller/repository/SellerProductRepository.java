package com.zipddak.seller.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.dto.CategoryDto;
import com.zipddak.dto.ProductDto;
import com.zipddak.entity.QCategory;
import com.zipddak.entity.QProduct;
import com.zipddak.seller.dto.SearchConditionDto;

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
	public List<ProductDto> searchMyProducts(PageRequest pageRequest, SearchConditionDto scDto) {

        QProduct product = QProduct.product;

        List<ProductDto> result = jpaQueryFactory.select(Projections.fields(ProductDto.class,
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
									                .where(QPredicate.eq(product.sellerUsername, scDto.getSellerUsername()),
									                		QPredicate.inBoolean(product.visibleYn, scDto.getVisibleList()),
									                	    QPredicate.inInt(product.categoryIdx, scDto.getCategoryList()),
									                	    QPredicate.contains(product.name, scDto.getKeyword()))
									                .orderBy(product.productIdx.desc())
									                .offset(pageRequest.getOffset())
									                .limit(pageRequest.getPageSize())
									                .fetch();

        return result;
    }

	//특정 셀러가 등록한 상품 수 
    public Long countMyProducts(SearchConditionDto scDto) {

        QProduct product = QProduct.product;

        return jpaQueryFactory.select(product.count())
				                .from(product)
				                .where(
				                        QPredicate.eq(product.sellerUsername, scDto.getSellerUsername()),
//				                        QPredicate.inInt(product.visibleYn, scDto.getVisibleList()),
				                        QPredicate.inInt(product.categoryIdx, scDto.getCategoryList()),
				                        QPredicate.contains(product.name, scDto.getKeyword()))
				                .fetchOne();
    }
}	
	
	
	
	
	
