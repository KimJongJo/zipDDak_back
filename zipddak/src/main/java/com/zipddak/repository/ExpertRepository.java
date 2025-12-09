package com.zipddak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.Expert;

public interface ExpertRepository extends JpaRepository<Expert, Integer> {

}
