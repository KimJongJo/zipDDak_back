package com.zipddak.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.Expert;

public interface SignExpertRepository extends JpaRepository<Expert, Integer> {

}
