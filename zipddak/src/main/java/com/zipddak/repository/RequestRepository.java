package com.zipddak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.Request;

public interface RequestRepository extends JpaRepository<Request, Integer>{

}
