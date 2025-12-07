package com.zipddak.seller.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SellerProductRepository {

	
	private final JPAQueryFactory jpaQueryFactory;
}
