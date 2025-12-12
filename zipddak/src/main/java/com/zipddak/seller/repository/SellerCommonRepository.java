package com.zipddak.seller.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zipddak.entity.OrderItem;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SellerCommonRepository {
	
	private final JPAQueryFactory jpaQueryFactory;




}
