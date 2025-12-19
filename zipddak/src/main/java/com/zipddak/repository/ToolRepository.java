package com.zipddak.repository;

<<<<<<< HEAD
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.Tool;

public interface ToolRepository extends JpaRepository<Tool, Integer> {
	
	Optional<Tool> findByOwner (String username)throws Exception;
=======
import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.Tool;

public interface ToolRepository extends JpaRepository<Tool, Integer> {
>>>>>>> refs/heads/main

}
