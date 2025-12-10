package com.zipddak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Integer> {

}
