package com.zipddak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zipddak.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	// 상위 카테고리 (depth = 1)
	// select * from category where depth = ㅇㅇ and type = ㅁㅁ
    List<Category> findByDepthAndType(int depth, String type);
    
    // 특정 상위 카테고리의 하위 카테고리
    // select * from category where parentIdx = ㅇㅇ and type = ㅁㅁ
    List<Category> findByParentIdxAndType(Integer parentIdx, String type);

}
