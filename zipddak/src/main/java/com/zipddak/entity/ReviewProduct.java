package com.zipddak.entity;

import java.sql.Date;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Entity
public class ReviewProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewProductIdx;

    @Column(nullable = false)
    private Integer score;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    private Date createdate;

    @Column
    private String writer; 

    @Column
    private Integer productIdx;

    @Column
    private Integer orderIdx;

    @Column
    private Integer img1;

    @Column
    private Integer img2;

    @Column
    private Integer img3;
}
