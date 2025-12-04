package com.zipddak.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.User;

public interface UserRepository extends JpaRepository<User, String>{

}
