package com.zipddak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.ProductOption;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Integer> {
	List<ProductOption> findByProduct_ProductIdx(Integer productId);
}
