package com.zipddak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.FavoritesTool;

public interface FavoritesToolRepository extends JpaRepository<FavoritesTool, Integer> {

}
