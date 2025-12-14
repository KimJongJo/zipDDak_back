package com.zipddak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.Estimate;

public interface EstimateRepository extends JpaRepository<Estimate, Integer> {

}
