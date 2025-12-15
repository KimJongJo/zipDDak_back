package com.zipddak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.Matching;

public interface MatchingRepository extends JpaRepository<Matching, Integer>{

	Matching findByMatchingCode(String asText);

}
