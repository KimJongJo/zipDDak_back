package com.zipddak.mypage.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderItemDslRRepository {

	private final JPAQueryFactory jpaQueryFactory;

}
