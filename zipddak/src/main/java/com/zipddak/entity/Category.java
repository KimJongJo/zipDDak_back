package com.zipddak.entity;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;

import com.zipddak.dto.CategoryDto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryIdx;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer depth;

    @Column
    private Integer parentIdx;
    
    @Column(nullable = false)
    private CategoryType type;
    
    
    public enum CategoryType {
        product, expert, community, tool
    }
    
    
//    public CategoryDto toDto() {
//		return CategoryDto.builder()
//				.categoryIdx(categoryIdx) //DTO의 변수명 (내가 갖고있는 변수명)
//				.name(name)
//				.depth(depth)
//				.parentIdx(parentIdx)
//				.type(type)
//				.build();
//	}
    
    
    
}
