package com.zipddak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
import com.google.common.base.Optional;
import com.zipddak.entity.Expert;

public interface ExpertRepository extends JpaRepository<Expert, Integer> {
	Optional<Expert> findByUser_Username(String username);
=======
import com.zipddak.entity.Expert;

public interface ExpertRepository extends JpaRepository<Expert, Integer> {

>>>>>>> branch 'main' of https://github.com/KimJongJo/zipDDak_back.git
}
