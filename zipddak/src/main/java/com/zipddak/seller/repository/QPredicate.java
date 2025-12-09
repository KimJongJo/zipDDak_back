package com.zipddak.seller.repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

// 공용 where 조건 유틸
public class QPredicate {

    // 일치
    public static BooleanExpression eq(StringPath path, String value) {
        return (value == null || value.isEmpty()) ? null : path.eq(value);
    }

    // 문자열
    public static BooleanExpression inString(StringPath path, List<String> values) {
        return (values == null || values.isEmpty()) ? null : path.in(values);
    }

    // 숫자
    public static BooleanExpression inInt(NumberPath<Integer> path, List<Integer> values) {
        return (values == null || values.isEmpty()) ? null : path.in(values);
    }

    // 포함
    public static BooleanExpression contains(StringPath path, String keyword) {
        return (keyword == null || keyword.isEmpty()) ? null : path.contains(keyword);
    }

    //날짜 검색용
    public static BooleanExpression dateEq(DatePath<java.sql.Date> path, java.sql.Date value) {
        return (value == null) ? null : path.eq(value);
    }

    // OR 포함 검색
    public static BooleanExpression anyContains(String keyword, StringPath... paths) {
        if (keyword == null || keyword.isEmpty())
            return null;

        BooleanExpression exp = null;
        for (StringPath p : paths) {
            BooleanExpression e = p.contains(keyword);
            exp = (exp == null) ? e : exp.or(e);
        }
        return exp;
    }
    
    
    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> BooleanExpression inEnum(EnumPath<E> path, List<String> values) {
        if (values == null || values.isEmpty()) return null;

        Class<E> enumClass = (Class<E>) path.getType();

        List<E> enums = values.stream()
                .map(v -> Enum.valueOf(enumClass, v))
                .collect(Collectors.toList());

        return path.in(enums);
    }

}
