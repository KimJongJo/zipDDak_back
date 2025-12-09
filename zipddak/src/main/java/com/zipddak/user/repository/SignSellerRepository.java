package com.zipddak.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.Seller;

public interface SignSellerRepository extends JpaRepository<Seller, Integer> {

}
