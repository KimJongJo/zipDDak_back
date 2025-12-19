package com.zipddak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.Rental;

public interface RentalRepository extends JpaRepository<Rental, Integer> {

	Rental findByRentalCode(String orderId);

}
